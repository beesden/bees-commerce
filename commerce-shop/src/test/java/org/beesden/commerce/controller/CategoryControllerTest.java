package org.beesden.commerce.controller;

import org.beesden.commerce.dao.CategoryRepository;
import org.beesden.commerce.model.CategoryDTO;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith( SpringRunner.class )
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
public class CategoryControllerTest extends AbstractControllerTest {

	private static final CategoryDTO TEST_CATEGORY_1 = new CategoryDTO( 1L, "category_shirt", null, null, "Shirts",
			null );
	private static final CategoryDTO TEST_CATEGORY_2 = new CategoryDTO( 2L, "category_trouser", "Trouser category",
			null, "Trousers", null );
	private static final CategoryDTO TEST_CATEGORY_3 = new CategoryDTO( 3L, "category_dress", null, "Dress category",
			"Dresses", null );

	private String CATEGORY_URL = "/categories";

	@Autowired
	private CategoryRepository categoryRepository;

	@Test
	public void categoryNotFound() throws Exception {
		mockMvc.perform( get( CATEGORY_URL + "/empty" ) ).andExpect( status().isNotFound() );
	}

	@Test
	public void crudCategory() throws Exception {
		mockMvc.perform(
				post( CATEGORY_URL ).contentType( contentType ).content( json( TEST_CATEGORY_3.toCategory() ) ) )
				.andExpect( status().isCreated() );

		mockMvc.perform( get( CATEGORY_URL + "/" + TEST_CATEGORY_3.getCategoryId() ) )
				.andExpect( jsonPath( "$.id", Matchers.equalTo( TEST_CATEGORY_3.getCategoryId() ) ) )
				.andExpect( status().isOk() );

		mockMvc.perform( delete( CATEGORY_URL + "/" + TEST_CATEGORY_3.getCategoryId() ) )
				.andExpect( status().isNoContent() );

		mockMvc.perform( get( CATEGORY_URL + "/" + TEST_CATEGORY_3.getCategoryId() ) )
				.andExpect( status().isNotFound() );
	}

	@Test
	public void listCategories() throws Exception {
		mockMvc.perform( get( CATEGORY_URL ) )
				.andExpect( status().isOk() ).andExpect( content().contentType( contentType ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 2 ) ) )
				.andExpect( jsonPath( "$.results[0].id", Matchers.is( TEST_CATEGORY_1.getCategoryId() ) ) )
				.andExpect( jsonPath( "$.results[0].title", Matchers.is( TEST_CATEGORY_1.getTitle() ) ) )
				.andExpect( jsonPath( "$.results[0].summary", Matchers.is( TEST_CATEGORY_1.getSummary() ) ) )
				.andExpect( jsonPath( "$.results[0].description", Matchers.is( TEST_CATEGORY_1.getDescription() ) ) )
				.andExpect( jsonPath( "$.results[1].id", Matchers.is( TEST_CATEGORY_2.getCategoryId() ) ) )
				.andExpect( jsonPath( "$.results[1].title", Matchers.is( TEST_CATEGORY_2.getTitle() ) ) )
				.andExpect( jsonPath( "$.results[1].summary", Matchers.is( TEST_CATEGORY_2.getSummary() ) ) )
				.andExpect( jsonPath( "$.results[1].description", Matchers.is( TEST_CATEGORY_2.getDescription() ) ) );
	}

	@Before
	public void setup() throws Exception {
		super.setup();

		categoryRepository.save( TEST_CATEGORY_1 );
		categoryRepository.save( TEST_CATEGORY_2 );
	}
}