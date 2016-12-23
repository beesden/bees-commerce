//package org.beesden.commerce.search;
//
//import org.beesden.commerce.common.Utils;
//import org.beesden.commerce.common.client.SearchClient;
//import org.beesden.commerce.common.model.EntityReference;
//import org.beesden.commerce.common.model.EntityType;
//import org.beesden.commerce.common.model.search.SearchDocument;
//import org.beesden.commerce.common.model.search.SearchForm;
//import org.beesden.commerce.common.model.search.SearchResultWrapper;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.annotation.PostConstruct;
//import java.util.HashMap;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@RunWith( SpringRunner.class )
//@SpringBootTest
//@ActiveProfiles( "test" )
//public class SearchClientTest {
//
//	@Autowired
//	private SearchClient searchClient;
//
//	@Test
//	public void facetsTest() throws Exception {
//
//		SearchForm searchForm = new SearchForm();
//		SearchResultWrapper results;
//		searchForm.setTypes( Utils.buildSet( EntityType.PRODUCT ) );
//
//		// Facet by type
//		results = searchClient.performSearch( searchForm );
//		assertThat( results.getTotal() ).isEqualTo( 3 );
//		assertThat( results.getFacets().get( "colour" ).size() ).isEqualTo( 3 );
//		assertThat( results.getFacets().get( "size" ).size() ).isEqualTo( 5 );
//
//		// Facet by multiple types
//		searchForm.setTypes( Utils.buildSet( EntityType.PRODUCT, EntityType.CATEGORY ) );
//		results = searchClient.performSearch( searchForm );
//		assertThat( results.getTotal() ).isEqualTo( 5 );
//
//		// Facet by colour
//		searchForm.setFacets( Utils.buildSet( "colour:red" ) );
//		results = searchClient.performSearch( searchForm );
//		assertThat( results.getTotal() ).isEqualTo( 2 );
//		assertThat( results.getFacets().size() ).isEqualTo( 2 );
//		assertThat( results.getFacets().get( "colour" ).size() ).isEqualTo( 3 );
//
//		// Facet by invalid colour
//		searchForm.setFacets( Utils.buildSet( "colour:orange" ) );
//		results = searchClient.performSearch( searchForm );
//		assertThat( results.getTotal() ).isEqualTo( 0 );
//		assertThat( results.getFacets().size() ).isEqualTo( 1 );
//
//		// Facet by colour and size
//		searchForm.setFacets( Utils.buildSet( "colour:red", "size:SM" ) );
//		results = searchClient.performSearch( searchForm );
//		assertThat( results.getTotal() ).isEqualTo( 1 );
//
//		// Facet by ID
//		searchForm.setFacets( null );
//		searchForm.setIds( Utils.buildSet( "p1109", "p1455" ) );
//		results = searchClient.performSearch( searchForm );
//		assertThat( results.getTotal() ).isEqualTo( 2 );
//	}
//
//	@Test
//	public void indexDocumentTest() throws Exception {
//
//		SearchDocument document = new SearchDocument();
//		document.setEntity( new EntityReference( EntityType.CATEGORY, "accessories" ) );
//		document.setTitle( "Some accessories" );
//
//		SearchForm searchForm = new SearchForm();
//		SearchResultWrapper results;
//		searchForm.setIds( Utils.buildSet( "accessories" ) );
//		searchForm.setTypes( Utils.buildSet( EntityType.CATEGORY ) );
//
//		// Index the document
//		searchClient.submitToIndex( document );
//
//		// Check document indexed
//		results = searchClient.performSearch( searchForm );
//		assertThat( results.getTotal() ).isEqualTo( 1 );
//		assertThat( results.getResults().get( 0 ).getTitle() ).isEqualTo( document.getTitle() );
//
//		// Update the document
//		document.setTitle( "All accessories" );
//		searchClient.submitToIndex( document );
//		results = searchClient.performSearch( searchForm );
//		assertThat( results.getTotal() ).isEqualTo( 1 );
//		assertThat( results.getResults().get( 0 ).getTitle() ).isEqualTo( document.getTitle() );
//
//		// Remove from index
//		searchClient.removeFromIndex( document.getEntity() );
//		results = searchClient.performSearch( searchForm );
//		assertThat( results.getTotal() ).isEqualTo( 0 );
//	}
//
//	@Test
//	public void keywordSearchTest() throws Exception {
//
//		SearchForm searchForm = new SearchForm();
//		SearchResultWrapper results;
//
//		// Search for empty results
//		searchForm.setTerm( "flower" );
//		results = searchClient.performSearch( searchForm );
//		assertThat( results.getTotal() ).isEqualTo( 0 );
//
//		// Search for fuzzy term
//		searchForm.setTerm( "kimon" );
//		results = searchClient.performSearch( searchForm );
//		assertThat( results.getTotal() ).isEqualTo( 3 );
//
//		// Search for weighted term
//		searchForm.setTerm( "fancy hat" );
//		results = searchClient.performSearch( searchForm );
//		assertThat( results.getTotal() ).isEqualTo( 2 );
//		assertThat( results.getResults().get( 0 ).getId() ).isEqualTo( "p1455" );
//
//		// Misspelled search
//		searchForm.setTerm( "kimoo" );
//		results = searchClient.performSearch( searchForm );
//		assertThat( results.getTotal() ).isEqualTo( 3 );
//
//		// Case insensitive search
//		searchForm.setTerm( "KiMOnO" );
//		results = searchClient.performSearch( searchForm );
//		assertThat( results.getTotal() ).isEqualTo( 3 );
//	}
//
//	@Test
//	public void paginationTest() throws Exception {
//
//		SearchForm searchForm = new SearchForm();
//		SearchResultWrapper results;
//		searchForm.setTypes( Utils.buildSet( EntityType.PRODUCT, EntityType.CATEGORY ) );
//
//		// List all results
//		results = searchClient.performSearch( searchForm );
//		assertThat( results.getResults().size() ).isEqualTo( 5 );
//		assertThat( results.getTotal() ).isEqualTo( 5 );
//
//		// Limit results
//		searchForm.setResults( 2 );
//		results = searchClient.performSearch( searchForm );
//		assertThat( results.getResults().size() ).isEqualTo( 2 );
//		assertThat( results.getTotal() ).isEqualTo( 5 );
//
//		// Paginate results
//		searchForm.setPage( 3 );
//		results = searchClient.performSearch( searchForm );
//		assertThat( results.getResults().size() ).isEqualTo( 1 );
//		assertThat( results.getTotal() ).isEqualTo( 5 );
//	}
//
//	@PostConstruct
//	public void populateIndex() throws Exception {
//
//		searchClient.clearIndex();
//		SearchDocument document = new SearchDocument();
//
//		document.setEntity( new EntityReference( EntityType.CATEGORY, "womens_clothes" ) );
//		document.setTitle( "Women's Clothes" );
//		document.setFacets( new HashMap<>() );
//		searchClient.submitToIndex( document );
//
//		document.setEntity( new EntityReference( EntityType.CATEGORY, "womens_kimonos" ) );
//		document.setTitle( "Women's Kimonos" );
//		document.setFacets( new HashMap<>() );
//		searchClient.submitToIndex( document );
//
//		document.setEntity( new EntityReference( EntityType.PRODUCT, "p1109" ) );
//		document.setTitle( "Sequin kimono" );
//		document.getFacets().put( "colour", Utils.buildSet( "red", "blue" ) );
//		document.getFacets().put( "size", Utils.buildSet( "XS", "SM", "M", "LG" ) );
//		searchClient.submitToIndex( document );
//
//		document.setEntity( new EntityReference( EntityType.PRODUCT, "p1455" ) );
//		document.setTitle( "Fancy Kimono Hat" );
//		document.getFacets().put( "colour", Utils.buildSet( "pink", "blue" ) );
//		document.getFacets().put( "size", Utils.buildSet( "XS", "M" ) );
//		searchClient.submitToIndex( document );
//
//		document.setEntity( new EntityReference( EntityType.PRODUCT, "p1955" ) );
//		document.setTitle( "Massive Pointy hat" );
//		document.getFacets().put( "colour", Utils.buildSet( "red" ) );
//		document.getFacets().put( "size", Utils.buildSet( "XL", "LG" ) );
//		searchClient.submitToIndex( document );
//
//	}
//}