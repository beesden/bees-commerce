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

		mockMvc.perform( get( SEARCH_URL ).param( "term", "*" ) )
				.andExpect( status().isOk() ).andExpect( content().contentType( contentType ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 5 ) ) );

		mockMvc.perform( get( SEARCH_URL ).param( "term", "*" ).param( "results", "2" ) )
				.andExpect( status().isOk() ).andExpect( content().contentType( contentType ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 2 ) ) );

		mockMvc.perform( get( SEARCH_URL ).param( "term", "*" ).param( "results", "2" ).param( "page", "3" ) )
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

		mockMvc.perform( get( SEARCH_URL ).param( "term", "kim" ) )
				.andExpect( status().isOk() ).andExpect( content().contentType( contentType ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 3 ) ) );

		mockMvc.perform( get( SEARCH_URL ).param( "term", "kimono clothes" ) )
				.andExpect( status().isOk() ).andExpect( content().contentType( contentType ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 4 ) ) );

	}

	@PostConstruct
	public void populateIndex() throws Exception {

		super.setup();

		SearchDocument document = new SearchDocument();

		searchService.clearIndex();

		document.setEntity( new EntityReference( "p1109", EntityType.PRODUCT ) );
		document.setTitle( "Sequin Kimono" );
		searchService.submitToIndex( document );

		document.setEntity( new EntityReference( "p1455", EntityType.PRODUCT ) );
		document.setTitle( "Fancy Kimono Hat" );
		searchService.submitToIndex( document );

		document.setEntity( new EntityReference( "p1955", EntityType.PRODUCT ) );
		document.setTitle( "Fancy pointy hat" );
		searchService.submitToIndex( document );

		document.setEntity( new EntityReference( "womens_clothes", EntityType.CATEGORY ) );
		document.setTitle( "Women's Clothes" );
		searchService.submitToIndex( document );

		document.setEntity( new EntityReference( "womens_kimonos", EntityType.CATEGORY ) );
		document.setTitle( "Women's Kimonos" );
		searchService.submitToIndex( document );

	}
}