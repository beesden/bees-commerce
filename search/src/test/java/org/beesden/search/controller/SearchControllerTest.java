package org.beesden.search.controller;

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
		document.setId( "accessories" );
		document.setTitle( "All accessories" );
		document.setEntityType( EntityType.CATEGORY );

		mockMvc.perform(
				post( SEARCH_URL ).contentType( contentType ).content( json( document ) ) )
				.andExpect( status().isCreated() );

		mockMvc.perform( get( SEARCH_URL ).param( "title", document.getTitle() ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 1 ) ) )
				.andExpect( jsonPath( "$.results[0].id", Matchers.equalTo( document.getTitle() ) ) )
				.andExpect( status().isOk() );

		//		mockMvc.perform( delete( SEARCH_URL + "/" + document ) )
		//				.andExpect( status().isNoContent() );
		//
		//		mockMvc.perform( get( SEARCH_URL + "/" + TEST_CATEGORY_3.getCategoryId() ) )
		//				.andExpect( jsonPath( "$.results", Matchers.hasSize( 0 ) ) );
	}

	@Test
	public void performSearchTest() throws Exception {

		//		mockMvc.perform( get( SEARCH_URL ).param( "title", "fancy kimono" ) )
		//				.andExpect( status().isOk() ).andExpect( content().contentType( contentType ) )
		//				.andExpect( jsonPath( "$.results", Matchers.hasSize( 1 ) ) );

		mockMvc.perform( get( SEARCH_URL ).param( "title", "kimono" ) )
				.andExpect( status().isOk() ).andExpect( content().contentType( contentType ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 2 ) ) );

		mockMvc.perform( get( SEARCH_URL ).param( "title", "kim" ) )
				.andExpect( status().isOk() ).andExpect( content().contentType( contentType ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 2 ) ) );

		mockMvc.perform( get( SEARCH_URL ).param( "title", "flower" ) )
				.andExpect( status().isOk() ).andExpect( content().contentType( contentType ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 0 ) ) );

	}

	@PostConstruct
	public void populateIndex() throws Exception {

		super.setup();

		SearchDocument document = new SearchDocument();

		document.setId( "p1109" );
		document.setTitle( "Sequin Kimono" );
		document.setEntityType( EntityType.PRODUCT );
		searchService.submitToIndex( document );

		document.setId( "p1455" );
		document.setTitle( "Fancy Kimono Hat" );
		document.setEntityType( EntityType.PRODUCT );
		searchService.submitToIndex( document );

		document.setId( "womens_clothes" );
		document.setTitle( "Women's Clothes" );
		document.setEntityType( EntityType.CATEGORY );
		searchService.submitToIndex( document );

	}
}