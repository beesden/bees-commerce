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
import org.beesden.commerce.common.Utils;
import org.beesden.commerce.common.client.SearchClient;
import org.beesden.commerce.common.model.EntityReference;
import org.beesden.commerce.common.model.search.SearchDocument;
import org.beesden.commerce.common.model.search.SearchForm;
import org.beesden.commerce.common.model.search.SearchResult;
import org.beesden.commerce.common.model.search.SearchResultWrapper;
import org.beesden.commerce.search.exception.SearchEntityException;
import org.beesden.commerce.search.exception.SearchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.Closeable;
import java.io.IOException;
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

    private List<SearchResultWrapper.SearchResultFacets> buildFacets(List<FacetResult> facets) {

        return facets.stream()
                .filter(Objects::nonNull)
                .map(facet -> {
                    SearchResultWrapper.SearchResultFacets facetsWrap = new SearchResultWrapper.SearchResultFacets();
                    facetsWrap.setName(facet.dim);

                    facetsWrap.setFacets(Arrays.stream(facet.labelValues)
                            .map(labelValue -> new SearchResultWrapper.SearchResultFacet(labelValue.label, labelValue.value.intValue()))
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

    public SearchResultWrapper performSearch(@Valid @RequestBody SearchForm searchForm) {

        SearchResultWrapper resultWrapper = new SearchResultWrapper();
        resultWrapper.setRequest(searchForm);

        DirectoryReader indexReader = null;
        TaxonomyReader taxoReader = null;

        try {
            indexReader = DirectoryReader.open(index);
            taxoReader = new DirectoryTaxonomyReader(taxoIndex);

            IndexSearcher searcher = new IndexSearcher(indexReader);
            BooleanQuery.Builder query = new BooleanQuery.Builder();

            // Filter by ID
            if (Utils.notNullOrEmpty(searchForm.getIds())) {
                BooleanQuery.Builder idQuery = new BooleanQuery.Builder();
                searchForm.getIds()
                        .forEach(id -> idQuery.add(new TermQuery(new Term("id", id)), BooleanClause.Occur.SHOULD));
                query.add(idQuery.build(), BooleanClause.Occur.MUST);
            }

            // Provide fuzzy searching for search terms
            if (searchForm.getTerm() != null) {
                BooleanQuery.Builder termQuery = new BooleanQuery.Builder();
                String searchTerm = searchForm.getTerm().toLowerCase();

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
            if (Utils.notNullOrEmpty(searchForm.getTypes())) {
                BooleanQuery.Builder typeQuery = new BooleanQuery.Builder();
                searchForm.getTypes()
                        .forEach(type -> typeQuery.add(new TermQuery(new Term("type", type.name())), BooleanClause.Occur.SHOULD));
                query.add(typeQuery.build(), BooleanClause.Occur.MUST);
            }

            TopDocs results;

            if (Utils.notNullOrEmpty(searchForm.getFacets())) {
                // Perform faceted search
                DrillDownQuery facetedQuery = new DrillDownQuery(facetConfig, query.build());
                searchForm.getFacets().forEach(facet -> {
                    String[] facetParts = facet.split(":");
                    if (facetParts.length == 2) {
                        facetedQuery.add(facetParts[0], facetParts[1]);
                    }
                });

                DrillSideways ds = new DrillSideways(searcher, facetConfig, taxoReader);
                DrillSideways.DrillSidewaysResult facetedResult = ds.search(facetedQuery, searchForm.getResults() * searchForm
                        .getPage());
                results = facetedResult.hits;
                resultWrapper.setFacets(buildFacets(facetedResult.facets.getAllDims(10)));

            } else {
                // Perform normal search
                results = searcher.search(query.build(), searchForm.getResults() * searchForm.getPage());

                // Collect facets separately
                FacetsCollector fc = new FacetsCollector();
                FacetsCollector.search(searcher, query.build(), 10, fc);
                Facets facets = new FastTaxonomyFacetCounts(taxoReader, facetConfig, fc);
                resultWrapper.setFacets(buildFacets(facets.getAllDims(10)));
            }

            resultWrapper.setResults(new ArrayList<>());
            resultWrapper.setTotal(results.totalHits);

            // Convert results
            Arrays.stream(results.scoreDocs).skip(searchForm.getStartIndex()).forEach(score -> {
                try {
                    Document document = searcher.doc(score.doc);

                    SearchResult result = new SearchResult();
                    result.setId(document.getField("id").stringValue());
                    result.setTitle(document.getField("title").stringValue());
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

            document.add(new StringField("id", searchDocument.getEntity().getId(), Field.Store.YES));
            document.add(new StringField("type", searchDocument.getEntity().getType().name(), Field.Store.YES));
            document.add(new TextField("title", searchDocument.getTitle(), Field.Store.YES));

            // Populate additional fields
            if (searchDocument.getFacets() != null) {
                searchDocument.getFacets().forEach((key, value) -> {
                    facetConfig.setMultiValued(key, true);
                    if (Utils.notNullOrEmpty(value)) {
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


