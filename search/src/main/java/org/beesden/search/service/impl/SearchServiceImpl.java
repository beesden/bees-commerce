package org.beesden.search.service.impl;

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
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.beesden.common.EntityReference;
import org.beesden.common.EntityType;
import org.beesden.common.model.SearchDocument;
import org.beesden.search.exception.SearchEntityException;
import org.beesden.search.exception.SearchException;
import org.beesden.search.model.SearchForm;
import org.beesden.search.model.SearchResult;
import org.beesden.search.model.SearchResultWrapper;
import org.beesden.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

@Service
@Slf4j
public class SearchServiceImpl implements SearchService {

	private FacetsConfig facetConfig = new FacetsConfig();
	private Analyzer analyzer = new StandardAnalyzer();
	private Directory index;
	private Directory taxoIndex;

	@Autowired
	public SearchServiceImpl( Directory index, Directory taxonomy ) {
		this.index = index;
		this.taxoIndex = taxonomy;
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
			if ( searchForm.getId() != null && searchForm.getId().length > 0 ) {
				query.add( new TermQuery( new Term( "id", String.join( " ", searchForm.getId() ).toLowerCase() ) ),
						BooleanClause.Occur.MUST );
			}

			// Provide fuzzy searching for search terms
			if ( searchForm.getTerm() != null ) {
				query.add( new FuzzyQuery( new Term( "title", searchForm.getTerm() ) ),
						BooleanClause.Occur.MUST );
			}

			// Filter by type
			if ( searchForm.getType() != null && searchForm.getType().length > 0 ) {
				query.add( new TermQuery( new Term( "type", String.join( " ", searchForm.getType() ).toLowerCase() ) ),
						BooleanClause.Occur.MUST );
			}

			TopDocs results;

			if ( searchForm.getFacet() != null ) {
				// Perform faceted search
				DrillDownQuery facetedQuery = new DrillDownQuery( facetConfig, query.build() );
				Arrays.stream( searchForm.getFacet() )
						.forEach( facet -> {
							String[] facetParts = facet.split( ":" );
							if ( facetParts.length == 2 ) {
								facetedQuery.add( facetParts[ 0 ], facetParts[ 1 ] );
							}
						} );

				DrillSideways ds = new DrillSideways( searcher, facetConfig, taxoReader );
				DrillSideways.DrillSidewaysResult facetedResult = ds
						.search( facetedQuery, searchForm.getResults() * searchForm.getPage() );
				results = facetedResult.hits;
				facetedResult.facets.getAllDims( 10 ).forEach( resultWrapper::addFacet );

			} else {
				// Perform normal search
				results = searcher.search( query.build(), searchForm.getResults() * searchForm.getPage() );

				// Collect facets separately
				FacetsCollector fc = new FacetsCollector();
				FacetsCollector.search( searcher, query.build(), 10, fc );
				Facets facets = new FastTaxonomyFacetCounts( taxoReader, facetConfig, fc );
				facets.getAllDims( 10 ).forEach( resultWrapper::addFacet );
			}

			resultWrapper.setTotal( results.totalHits );

			// Convert results
			for ( int i = searchForm.getStartIndex(); i < results.scoreDocs.length; i++ ) {
				resultWrapper.addResult( new SearchResult( searcher.doc( results.scoreDocs[ i ].doc ) ) );
			}

		} catch ( IOException e ) {
			throw new SearchException( "Error performing search", e );
		}

		return resultWrapper;
	}

	@PostConstruct
	public void populateIndex() throws Exception {

		clearIndex();

		// Create a huge index!
		SecureRandom random = new SecureRandom();
		Random rn = new Random();

		for ( int i = 3000; i < 5000; i++ ) {
			SearchDocument document = new SearchDocument();
			document.setEntity( new EntityReference( "p" + i, EntityType.PRODUCT ) );
			document.setTitle( new BigInteger( 130, random ).toString( 32 ) );
			int col = rn.nextInt( 5 );
			document.addFacet( "colour",
					col == 0 ? "red" : col == 1 ? "yellow" : col == 2 ? "blue" : col == 3 ? "pink" : col == 4 ? "green" :
							"purple" );
			col = rn.nextInt( 5 );
			document.addFacet( "size",
					col == 0 ? "XS" : col == 1 ? "M" : col == 2 ? "LG" : col == 3 ? "XL" : col == 4 ? "SM" :
							"" );
			submitToIndex( document );
		}

		String[] categories = { "Accessories", "All Accessories", "Jackets & Coats", "Jewellery", "All Sale", "All Shoes", "Skirts", "Tops", "Trousers & Shorts", "Bags", "Boots", "Cardigans", "Casual", "Clothing", "Clutch Bags", "Coats", "Dresses", "Flat Shoes", "Formal", "Gifts", "Hats & Gloves", "High Heels", "Jackets", "Playsuits & Jumpsuits", "Cardigans & Jumpers", "Tops", "Dresses", "Lace Dresses", "Maxi Dresses", "New Vintage", "New In", "Accessories", "Clothing", "Sale", "Accessories", "Clothing", "Coats & Jackets", "Denim", "Dresses", "Shoes", "Skirts", "Tops", "Trousers & Shorts", "Scarves", "Shoes", "Tights & Socks", "Watches", "Wraps and Capes" };
		for ( String c : categories ) {
			SearchDocument document = new SearchDocument();
			document.setEntity( new EntityReference( c.replace( " ", "_" ).replace( "[^\\w_]", "" ).toLowerCase(),
					EntityType.CATEGORY ) );
			document.setTitle( c );
			submitToIndex( document );
		}
	}

	@Override
	public void removeFromIndex( EntityReference entity ) {

		try {
			IndexWriter writer = new IndexWriter( index, new IndexWriterConfig( analyzer ) );

			// Construct query
			BooleanQuery.Builder query = new BooleanQuery.Builder();
			query.add( new TermQuery( new Term( "id", entity.getId() ) ), BooleanClause.Occur.MUST );
			query.add( new TermQuery( new Term( "type", entity.getType().name().toLowerCase() ) ),
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
			document.add( new StringField( "type", searchDocument.getEntity().getType().name().toLowerCase(),
					Field.Store.YES ) );
			document.add( new TextField( "title", searchDocument.getTitle(), Field.Store.YES ) );

			// Populate additional fields
			searchDocument.getFacets().entrySet().forEach( f -> {
				facetConfig.setMultiValued( f.getKey(), true );
				if ( f.getValue() != null ) {
					f.getValue().forEach( val -> {
						document.add( new FacetField( f.getKey(), val ) );
						document.add( new StoredField( f.getKey(), val ) );
					} );
				}
			} );

			writer.addDocument( facetConfig.build( taxoWriter, document ) );

			taxoWriter.close();
			writer.close();

		} catch ( IOException e ) {
			throw new SearchEntityException( "Error adding entity to index", searchDocument.getEntity(), e );
		}
	}

}


