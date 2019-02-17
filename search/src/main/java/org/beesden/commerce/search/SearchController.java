package org.beesden.commerce.search;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.facet.*;
import org.apache.lucene.facet.taxonomy.FastTaxonomyFacetCounts;
import org.apache.lucene.facet.taxonomy.TaxonomyReader;
import org.apache.lucene.facet.taxonomy.TaxonomyWriter;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.beesden.commerce.common.client.SearchClient;
import org.beesden.commerce.common.model.EntityReference;
import org.beesden.commerce.common.model.SearchRequest;
import org.beesden.commerce.common.model.resource.SearchDocument;
import org.beesden.commerce.common.model.resource.SearchResource;
import org.beesden.commerce.common.model.resource.SearchResource.SearchResult;
import org.beesden.commerce.common.model.resource.SearchResource.SearchResultFacet;
import org.beesden.commerce.common.model.resource.SearchResource.SearchResultFacets;
import org.beesden.commerce.search.exception.SearchEntityException;
import org.beesden.commerce.search.exception.SearchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.Closeable;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class SearchController implements SearchClient {

    private final FacetsConfig facetConfig = new FacetsConfig();
    private final Analyzer analyzer = new StandardAnalyzer();
    private final Directory index;
    private final Directory taxoIndex;

    @Autowired
    public SearchController(Directory index, Directory taxonomy) {
        this.index = index;
        this.taxoIndex = taxonomy;
    }

    private <T extends TwoPhaseCommit & Closeable> void commitChanges(T writer) {
        if (writer != null) {
            try {
                writer.commit();
                writer.close();
            } catch (IOException e) {
                try {
                    writer.rollback();
                } catch (IOException el) {
                    el.printStackTrace();
                }
            }
        }
    }

    private <T extends Closeable> void closeReader(T reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<SearchResultFacets> buildFacets(List<FacetResult> facets) {

        return facets.stream()
                .filter(Objects::nonNull)
                .map(facet -> {
                    SearchResultFacets facetsWrap = new SearchResultFacets();
                    facetsWrap.setName(facet.dim);

                    facetsWrap.setFacets(Arrays.stream(facet.labelValues)
                            .map(labelValue -> new SearchResultFacet(labelValue.label, labelValue.value.intValue()))
                            .collect(Collectors.toList()));

                    return facetsWrap;
                })
                .collect(Collectors.toList());
    }

    public void clearIndex() {

        IndexWriter writer = null;

        try {
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            writer = new IndexWriter(index, config);
            writer.deleteAll();

        } catch (IOException e) {
            throw new SearchException("Error clearing the index", e);
        } finally {
            commitChanges(writer);
        }
    }

    public SearchResource performSearch(@Valid @RequestBody SearchRequest searchRequest) {

        SearchResource resultWrapper = new SearchResource();
        resultWrapper.setRequest(searchRequest);

        DirectoryReader indexReader = null;
        TaxonomyReader taxoReader = null;

        try {
            indexReader = DirectoryReader.open(index);
            taxoReader = new DirectoryTaxonomyReader(taxoIndex);

            IndexSearcher searcher = new IndexSearcher(indexReader);
            BooleanQuery.Builder query = new BooleanQuery.Builder();

            // Filter by ID
            if (!CollectionUtils.isEmpty(searchRequest.getIds())) {
                BooleanQuery.Builder idQuery = new BooleanQuery.Builder();
                searchRequest.getIds()
                        .forEach(id -> idQuery.add(new TermQuery(new Term("id", id)), BooleanClause.Occur.SHOULD));
                query.add(idQuery.build(), BooleanClause.Occur.MUST);
            }

            // Sort results
            Sort sort = new Sort(SortField.FIELD_SCORE);
            SearchRequest.SearchSortField searchSort = searchRequest.getSortField();
            switch (searchSort) {
                case TITLE:
                    sort.setSort(SortField.FIELD_SCORE, new SortField(searchSort.getValue(), SortField.Type.STRING, false));
                    break;
                case DATE:
                case VALUE:
                    SortField sortField = new SortField(searchSort.getValue(), SortField.Type.LONG, false);
                    sortField.setMissingValue(Long.MAX_VALUE);
                    sort.setSort(SortField.FIELD_SCORE, sortField);
                    break;
            }

            // Provide fuzzy searching for search terms
            if (searchRequest.getTerm() != null) {
                BooleanQuery.Builder termQuery = new BooleanQuery.Builder();
                String searchTerm = searchRequest.getTerm().toLowerCase();

                // Adds weight to exact match
                termQuery.add(new WildcardQuery(new Term("title", searchTerm)), BooleanClause.Occur.SHOULD);

                // Add fuzzy / term searching
                Arrays.stream(searchTerm.split(" +")).forEach(term -> {
                    termQuery.add(new FuzzyQuery(new Term("title", term.toLowerCase())), BooleanClause.Occur.SHOULD);
                    termQuery.add(new TermQuery(new Term("title", term)), BooleanClause.Occur.SHOULD);
                });
                query.add(termQuery.build(), BooleanClause.Occur.MUST);
            }

            // Filter by type
            if (!CollectionUtils.isEmpty(searchRequest.getTypes())) {
                BooleanQuery.Builder typeQuery = new BooleanQuery.Builder();
                searchRequest.getTypes()
                        .forEach(type -> typeQuery.add(new TermQuery(new Term("type", type.name())), BooleanClause.Occur.SHOULD));
                query.add(typeQuery.build(), BooleanClause.Occur.MUST);
            }

            TopDocs results;

            if (!CollectionUtils.isEmpty(searchRequest.getFacets())) {
                // Perform faceted search
                DrillDownQuery facetedQuery = new DrillDownQuery(facetConfig, query.build());
                searchRequest.getFacets().forEach(facet -> {
                    String[] facetParts = facet.split(":");
                    if (facetParts.length == 2) {
                        facetedQuery.add(facetParts[0], facetParts[1]);
                    }
                });

                DrillSideways ds = new DrillSideways(searcher, facetConfig, taxoReader);
                DrillSideways.DrillSidewaysResult facetedResult = ds.search(facetedQuery, null, null, searchRequest.getEndIndex(), sort, false, false);
                results = facetedResult.hits;
                resultWrapper.setFacets(buildFacets(facetedResult.facets.getAllDims(10)));

            } else {
                // Perform normal search
                results = searcher.search(query.build(), searchRequest.getEndIndex(), sort);

                // Collect facets separately
                FacetsCollector fc = new FacetsCollector();
                FacetsCollector.search(searcher, query.build(), 10, fc);
                Facets facets = new FastTaxonomyFacetCounts(taxoReader, facetConfig, fc);
                resultWrapper.setFacets(buildFacets(facets.getAllDims(10)));
            }

            resultWrapper.setResults(new ArrayList<>());
            resultWrapper.setTotal((long) results.totalHits);

            // Convert results
            Arrays.stream(results.scoreDocs).skip(searchRequest.getStartIndex()).forEach(score -> {
                try {
                    Document document = searcher.doc(score.doc);

                    SearchResult result = new SearchResult();
                    result.setId(document.getField("id").stringValue());
                    result.setTitle(document.getField("title").stringValue());
                    result.setValue(document.getField("value").numericValue().doubleValue());
                    result.setMetadata(document.getFields()
                            .stream()
                            .filter(field -> field.fieldType().indexOptions() == IndexOptions.NONE)
                            .collect(Collectors.groupingBy(IndexableField::name, Collectors.mapping(IndexableField::stringValue, Collectors.toList()))));
                    resultWrapper.getResults().add(result);
                } catch (IOException ignored) {
                }
            });

        } catch (IndexNotFoundException e) {
            resultWrapper.setResults(new ArrayList<>());
        } catch (IOException e) {
            throw new SearchException("Error performing search", e);
        } finally {
            closeReader(indexReader);
            closeReader(taxoReader);
        }

        return resultWrapper;
    }

    public void removeFromIndex(@Valid @RequestBody EntityReference entity) {

        if (entity == null) {
            throw new SearchEntityException("No entitry reference provided");
        }

        IndexWriter writer = null;
        try {
            writer = new IndexWriter(index, new IndexWriterConfig(analyzer));

            // Construct query
            BooleanQuery.Builder query = new BooleanQuery.Builder();
            query.add(new TermQuery(new Term("id", entity.getId())), BooleanClause.Occur.MUST);
            query.add(new TermQuery(new Term("type", entity.getType().name())), BooleanClause.Occur.MUST);

            writer.deleteDocuments(query.build());

        } catch (IOException e) {
            throw new SearchEntityException("Error removing entity from index", entity, e);
        } finally {
            commitChanges(writer);
        }

    }

    public void submitToIndex(@Valid @RequestBody SearchDocument searchDocument) {

        removeFromIndex(searchDocument.getEntity());
        IndexWriter writer = null;
        TaxonomyWriter taxoWriter = null;

        try {
            writer = new IndexWriter(index, new IndexWriterConfig(analyzer));
            taxoWriter = new DirectoryTaxonomyWriter(taxoIndex);

            Document document = new Document();

            // Entity information
            document.add(new StringField("id", searchDocument.getEntity().getId(), Field.Store.YES));
            document.add(new StringField("type", searchDocument.getEntity().getType().name(), Field.Store.YES));

            // Indexed fields
            document.add(new TextField("title", searchDocument.getTitle(), Field.Store.YES));
            document.add(new StoredField("value", Optional.ofNullable(searchDocument.getValue()).orElse(0d)));

            // Sort title field
            document.add(new SortedDocValuesField(SearchRequest.SearchSortField.TITLE.getValue(), new BytesRef(searchDocument.getTitle())));

            // Sort date field
            if (searchDocument.getDate() != null) {
                document.add(new NumericDocValuesField(SearchRequest.SearchSortField.DATE.getValue(), Timestamp.valueOf(searchDocument.getDate()).getTime()));
            }

            // Sort value field
            if (searchDocument.getValue() != null) {
                document.add(new NumericDocValuesField(SearchRequest.SearchSortField.VALUE.getValue(), Math.round(searchDocument.getValue() * 100)));
            }

            // Populate additional fields
            if (searchDocument.getFacets() != null) {
                searchDocument.getFacets().forEach((key, value) -> {
                    facetConfig.setMultiValued(key, true);
                    if (!CollectionUtils.isEmpty(value)) {
                        value.stream().filter(Objects::nonNull).forEach(val -> {
                            document.add(new FacetField(key, val));
                            document.add(new StoredField(key, val));
                        });
                    }
                });
            }

            writer.addDocument(facetConfig.build(taxoWriter, document));
        } catch (IllegalArgumentException | IOException e) {
            throw new SearchEntityException("Error adding entity to index", searchDocument.getEntity(), e);
        } finally {
            commitChanges(writer);
            commitChanges(taxoWriter);
        }
    }

}


