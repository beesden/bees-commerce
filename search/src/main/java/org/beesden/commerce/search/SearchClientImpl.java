package org.beesden.commerce.search;

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
import org.beesden.commerce.search.exception.SearchEntityException;
import org.beesden.commerce.search.exception.SearchException;
import org.beesden.common.Utils;
import org.beesden.common.model.EntityReference;
import org.beesden.common.model.search.SearchDocument;
import org.beesden.common.model.search.SearchForm;
import org.beesden.common.model.search.SearchResult;
import org.beesden.common.model.search.SearchResultWrapper;
import org.beesden.common.service.SearchClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SearchClientImpl implements SearchClient {

	private FacetsConfig facetConfig = new FacetsConfig();
	private Analyzer analyzer = new StandardAnalyzer();
	private Directory index;
	private Directory taxoIndex;

	@Autowired
	public SearchClientImpl( Directory index, Directory taxonomy ) {
		this.index = index;
		this.taxoIndex = taxonomy;
	}

	private Map<String, Map<String, Integer>> buildFacets( List<FacetResult> facets ) {
		return facets.stream()
				.filter( facet -> facet != null )
				.collect( Collectors.toMap( facet -> facet.dim, facet -> Arrays.stream( facet.labelValues )
						.collect( Collectors.toMap( fa -> fa.label, fa -> fa.value.intValue() ) ) ) );
	}

	@Override
	public void clearIndex() {

		try {
			IndexWriterConfig config = new IndexWriterConfig( analyzer );
			IndexWriter writer = new IndexWriter( index, config );

			writer.deleteAll();
			writer.close();

		} catch ( IOException e ) {
			throw new SearchException( "Error clearing the index", e );
		}
	}

	@Override
	public SearchResultWrapper performSearch( SearchForm searchForm ) {

		SearchResultWrapper resultWrapper = new SearchResultWrapper();

		try {
			DirectoryReader indexReader = DirectoryReader.open( index );
			IndexSearcher searcher = new IndexSearcher( indexReader );
			TaxonomyReader taxoReader = new DirectoryTaxonomyReader( taxoIndex );

			// Construct base query
			BooleanQuery.Builder query = new BooleanQuery.Builder();

			// Filter by ID
			if ( Utils.notNullOrEmpty( searchForm.getIds() ) ) {
				BooleanQuery.Builder idQuery = new BooleanQuery.Builder();
				searchForm.getIds().forEach(
						id -> idQuery.add( new TermQuery( new Term( "id", id ) ), BooleanClause.Occur.SHOULD ) );
				query.add( idQuery.build(), BooleanClause.Occur.MUST );
			}

			// Provide fuzzy searching for search terms
			if ( searchForm.getTerm() != null ) {
				BooleanQuery.Builder termQuery = new BooleanQuery.Builder();
				String searchTerm = searchForm.getTerm().toLowerCase();

				// Adds weight to exact match
				termQuery
						.add( new WildcardQuery( new Term( "title", searchTerm ) ), BooleanClause.Occur.SHOULD );

				// Add fuzzy / term searching
				Arrays.stream( searchTerm.split( " +" ) ).forEach( term -> {
							termQuery.add( new FuzzyQuery( new Term( "title", term.toLowerCase() ) ),
									BooleanClause.Occur.SHOULD );
							termQuery.add( new TermQuery( new Term( "title", term ) ),
									BooleanClause.Occur.SHOULD );
						}
				);
				query.add( termQuery.build(), BooleanClause.Occur.MUST );
			}

			// Filter by type
			if ( Utils.notNullOrEmpty( searchForm.getTypes() ) ) {
				BooleanQuery.Builder typeQuery = new BooleanQuery.Builder();
				searchForm.getTypes().forEach(
						type -> typeQuery.add( new TermQuery( new Term( "type", type.name() ) ),
								BooleanClause.Occur.SHOULD ) );
				query.add( typeQuery.build(), BooleanClause.Occur.MUST );
			}

			TopDocs results;

			if ( Utils.notNullOrEmpty( searchForm.getFacets() ) ) {
				// Perform faceted search
				DrillDownQuery facetedQuery = new DrillDownQuery( facetConfig, query.build() );
				searchForm.getFacets().forEach( facet -> {
					String[] facetParts = facet.split( ":" );
					if ( facetParts.length == 2 ) {
						facetedQuery.add( facetParts[ 0 ], facetParts[ 1 ] );
					}
				} );

				DrillSideways ds = new DrillSideways( searcher, facetConfig, taxoReader );
				DrillSideways.DrillSidewaysResult facetedResult = ds
						.search( facetedQuery, searchForm.getResults() * searchForm.getPage() );
				results = facetedResult.hits;
				resultWrapper.setFacets( buildFacets( facetedResult.facets.getAllDims( 10 ) ) );

			} else {
				// Perform normal search
				results = searcher.search( query.build(), searchForm.getResults() * searchForm.getPage() );

				// Collect facets separately
				FacetsCollector fc = new FacetsCollector();
				FacetsCollector.search( searcher, query.build(), 10, fc );
				Facets facets = new FastTaxonomyFacetCounts( taxoReader, facetConfig, fc );
				resultWrapper.setFacets( buildFacets( facets.getAllDims( 10 ) ) );
			}

			resultWrapper.setResults( new ArrayList<>() );
			resultWrapper.setTotal( results.totalHits );

			// Convert results
			for ( int i = searchForm.getStartIndex(); i < results.scoreDocs.length; i++ ) {
				Document document = searcher.doc( results.scoreDocs[ i ].doc );

				SearchResult result = new SearchResult();
				result.setId( document.getField( "id" ).stringValue() );
				result.setTitle( document.getField( "title" ).stringValue() );
				result.setMetadata(
						document.getFields().stream()
								.filter( field -> field.fieldType().indexOptions() == IndexOptions.NONE )
								.collect( Collectors.groupingBy( IndexableField::name,
										Collectors.mapping( IndexableField::stringValue, Collectors.toList() ) ) ) );
				resultWrapper.getResults().add( result );
			}

		} catch ( IndexNotFoundException e ) {
			resultWrapper.setResults( new ArrayList<>() );
		} catch ( IOException e ) {
			throw new SearchException( "Error performing search", e );
		}

		return resultWrapper;
	}

	@Override
	public void removeFromIndex( EntityReference entity ) {

		try {
			IndexWriter writer = new IndexWriter( index, new IndexWriterConfig( analyzer ) );

			// Construct query
			BooleanQuery.Builder query = new BooleanQuery.Builder();
			query.add( new TermQuery( new Term( "id", entity.getId() ) ), BooleanClause.Occur.MUST );
			query.add( new TermQuery( new Term( "type", entity.getType().name() ) ),
					BooleanClause.Occur.MUST );

			writer.deleteDocuments( query.build() );
			writer.close();

		} catch ( IOException e ) {
			throw new SearchEntityException( "Error removing entity from index", entity, e );
		}

	}

	@Override
	public void submitToIndex( SearchDocument searchDocument ) {

		removeFromIndex( searchDocument.getEntity() );

		try {
			IndexWriter writer = new IndexWriter( index, new IndexWriterConfig( analyzer ) );
			TaxonomyWriter taxoWriter = new DirectoryTaxonomyWriter( taxoIndex );

			Document document = new Document();

			document.add( new StringField( "id", searchDocument.getEntity().getId(), Field.Store.YES ) );
			document.add( new StringField( "type", searchDocument.getEntity().getType().name(),
					Field.Store.YES ) );
			document.add( new TextField( "title", searchDocument.getTitle(), Field.Store.YES ) );

			// Populate additional fields
			if ( searchDocument.getFacets() != null ) {
				searchDocument.getFacets().entrySet().forEach( f -> {
					facetConfig.setMultiValued( f.getKey(), true );
					if ( Utils.notNullOrEmpty( f.getValue() ) ) {
						f.getValue().forEach( val -> {
							document.add( new FacetField( f.getKey(), val ) );
							document.add( new StoredField( f.getKey(), val ) );
						} );
					}
				} );
			}

			writer.addDocument( facetConfig.build( taxoWriter, document ) );

			taxoWriter.close();
			writer.close();

		} catch ( IOException e ) {
			throw new SearchEntityException( "Error adding entity to index", searchDocument.getEntity(), e );
		}
	}

}


