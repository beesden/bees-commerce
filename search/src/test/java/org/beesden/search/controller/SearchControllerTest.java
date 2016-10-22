package org.beesden.search.controller;

import org.beesden.common.EntityReference;
import org.beesden.common.EntityType;
import org.beesden.common.model.SearchDocument;
import org.beesden.search.service.SearchService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith( SpringRunner.class )
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
@ActiveProfiles( "test" )
public class SearchControllerTest extends AbstractTestController {

	private String SEARCH_URL = "/";

	@Autowired
	private SearchService searchService;

	@Test
	public void facetedSearchTest() throws Exception {

		mockMvc.perform( get( SEARCH_URL ).param( "term", "kimono" ).param( "facet", "colour:red" ) )
				.andExpect( status().isOk() ).andExpect( content().contentType( contentType ) )
				.andExpect( jsonPath( "$.facets[0].fields.blue", Matchers.equalTo( 2 ) ) )
				.andExpect( jsonPath( "$.facets", Matchers.hasSize( 2 ) ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 1 ) ) );

		mockMvc.perform( get( SEARCH_URL ).param( "term", "kimono" ).param( "facet", "colour:RED" ) )
				.andExpect( status().isOk() ).andExpect( content().contentType( contentType ) )
				.andExpect( jsonPath( "$.facets", Matchers.hasSize( 1 ) ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 0 ) ) );

		mockMvc.perform( get( SEARCH_URL ).param( "term", "kimono" ).param( "facet", "size:LG" ) )
				.andExpect( status().isOk() ).andExpect( content().contentType( contentType ) )
				.andExpect( jsonPath( "$.facets[1].fields.blue", Matchers.equalTo( 1 ) ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 1 ) ) );

		mockMvc.perform(
				get( SEARCH_URL ).param( "term", "kimono" ).param( "facet", "colour:blue" )
						.param( "facet", "size:LG" ) )
				.andExpect( status().isOk() ).andExpect( content().contentType( contentType ) )
				.andExpect( jsonPath( "$.facets[0].fields.blue", Matchers.equalTo( 1 ) ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 1 ) ) );

		mockMvc.perform(
				get( SEARCH_URL ).param( "id", "p1109" ) )
				.andExpect( status().isOk() ).andExpect( content().contentType( contentType ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 1 ) ) );

		mockMvc.perform(
				get( SEARCH_URL ).param( "type", "product" ) )
				.andExpect( status().isOk() ).andExpect( content().contentType( contentType ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 3 ) ) );

		mockMvc.perform(
				get( SEARCH_URL ).param( "type", "PRODUCT" ) )
				.andExpect( status().isOk() ).andExpect( content().contentType( contentType ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 3 ) ) );

	}

	@Test
	public void indexDocumentTest() throws Exception {

		SearchDocument document = new SearchDocument();
		document.setEntity( new EntityReference( "accessories", EntityType.CATEGORY ) );
		document.setTitle( "All accessories" );

		mockMvc.perform(
				post( SEARCH_URL ).contentType( contentType ).content( json( document ) ) )
				.andExpect( status().isCreated() );

		mockMvc.perform( get( SEARCH_URL ).param( "id", document.getEntity().getId() ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 1 ) ) )
				.andExpect( jsonPath( "$.results[0].title", Matchers.equalTo( document.getTitle() ) ) )
				.andExpect( status().isOk() );

		// TODO - fix this test!
		searchService.clearIndex();
		populateIndex();

		//		mockMvc.perform( delete( SEARCH_URL + "/" + document ) )
		//				.andExpect( status().isNoContent() );
		//
		//		mockMvc.perform( get( SEARCH_URL + "/" + TEST_CATEGORY_3.getCategoryId() ) )
		//				.andExpect( jsonPath( "$.results", Matchers.hasSize( 0 ) ) );
	}

	@Test
	public void performPaginationTest() throws Exception {

		mockMvc.perform( get( SEARCH_URL ).param( "term", "kimono" ) )
				.andExpect( status().isOk() ).andExpect( content().contentType( contentType ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 3 ) ) );

		mockMvc.perform( get( SEARCH_URL ).param( "term", "kimono" ).param( "results", "2" ) )
				.andExpect( status().isOk() ).andExpect( content().contentType( contentType ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 2 ) ) );

		mockMvc.perform( get( SEARCH_URL ).param( "term", "kimono" ).param( "results", "2" ).param( "page", "2" ) )
				.andExpect( status().isOk() ).andExpect( content().contentType( contentType ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 1 ) ) );

	}

	@Test
	public void performSearchTest() throws Exception {

		mockMvc.perform( get( SEARCH_URL ) )
				.andExpect( status().isOk() ).andExpect( content().contentType( contentType ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 0 ) ) );

		mockMvc.perform( get( SEARCH_URL ).param( "term", "flower" ) )
				.andExpect( status().isOk() ).andExpect( content().contentType( contentType ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 0 ) ) );

		mockMvc.perform( get( SEARCH_URL ).param( "term", "pointy" ) )
				.andExpect( status().isOk() ).andExpect( content().contentType( contentType ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 1 ) ) );

		mockMvc.perform( get( SEARCH_URL ).param( "term", "fancy" ) )
				.andExpect( status().isOk() ).andExpect( content().contentType( contentType ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 2 ) ) );

		mockMvc.perform( get( SEARCH_URL ).param( "term", "kimono" ) )
				.andExpect( status().isOk() ).andExpect( content().contentType( contentType ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 3 ) ) );

		mockMvc.perform( get( SEARCH_URL ).param( "term", "kimoo" ) )
				.andExpect( status().isOk() ).andExpect( content().contentType( contentType ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 3 ) ) );

		mockMvc.perform( get( SEARCH_URL ).param( "term", "black kimono" ) )
				.andExpect( status().isOk() ).andExpect( content().contentType( contentType ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 0 ) ) );

	}

	@PostConstruct
	public void populateIndex() throws Exception {

		super.setup();

		SearchDocument document = new SearchDocument();

		searchService.clearIndex();

		document.setEntity( new EntityReference( "p1109", EntityType.PRODUCT ) );
		document.setTitle( "Sequin Kimono" );
		document.setFacets( new HashMap<>() );
		document.addFacet( "colour", "red" );
		document.addFacet( "colour", "blue" );
		document.addFacet( "size", "M" );
		document.addFacet( "size", "SM" );
		document.addFacet( "size", "LG" );
		searchService.submitToIndex( document );

		document.setEntity( new EntityReference( "p1455", EntityType.PRODUCT ) );
		document.setTitle( "Fancy Kimono Hat" );
		document.setFacets( new HashMap<>() );
		document.addFacet( "colour", "blue" );
		document.addFacet( "colour", "pink" );
		document.addFacet( "size", "SM" );
		document.addFacet( "size", "XS" );
		searchService.submitToIndex( document );

		document.setEntity( new EntityReference( "p1955", EntityType.PRODUCT ) );
		document.setTitle( "Fancy pointy hat" );
		document.setFacets( new HashMap<>() );
		document.addFacet( "colour", "red" );
		document.addFacet( "size", "XL" );
		document.addFacet( "size", "LG" );
		searchService.submitToIndex( document );

		document.setEntity( new EntityReference( "womens_clothes", EntityType.CATEGORY ) );
		document.setTitle( "Women's Clothes" );
		document.setFacets( new HashMap<>() );
		searchService.submitToIndex( document );

		document.setEntity( new EntityReference( "womens_kimonos", EntityType.CATEGORY ) );
		document.setTitle( "Women's Kimonos" );
		document.setFacets( new HashMap<>() );
		searchService.submitToIndex( document );

	}
}