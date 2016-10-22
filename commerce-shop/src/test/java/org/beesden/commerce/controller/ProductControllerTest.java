package org.beesden.commerce.controller;

import org.beesden.commerce.dao.ProductRepository;
import org.beesden.commerce.model.ProductDTO;
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
public class ProductControllerTest extends AbstractControllerTest {

	private static final ProductDTO TEST_PRODUCT_1 = new ProductDTO( 1L, "p3556", null, null, "Test Product 1555",
			null, null );
	private static final ProductDTO TEST_PRODUCT_2 = new ProductDTO( 2L, "p2885", "Test product with desc",
			"Test product with summary", "Test Product 1555", null, null );
	private static final ProductDTO TEST_PRODUCT_3 = new ProductDTO( 3L, "p1555", null,
			"This product has a summary", "Test Product 1555", null, null );

	private String PRODUCT_URL = "/products";

	@Autowired
	private ProductRepository productRepository;

	@Test
	public void crudProduct() throws Exception {
		mockMvc.perform( post( PRODUCT_URL ).contentType( contentType ).content( json( TEST_PRODUCT_3.toProduct() ) ) )
				.andExpect( status().isCreated() );

		mockMvc.perform( get( PRODUCT_URL + "/" + TEST_PRODUCT_3.getProductKey() ) )
				.andExpect( jsonPath( "$.id", Matchers.equalTo( TEST_PRODUCT_3.getProductKey() ) ) )
				.andExpect( status().isOk() );

		mockMvc.perform( delete( PRODUCT_URL + "/" + TEST_PRODUCT_3.getProductKey() ) )
				.andExpect( status().isNoContent() );

		mockMvc.perform( get( PRODUCT_URL + "/" + TEST_PRODUCT_3.getProductKey() ) )
				.andExpect( status().isNotFound() );
	}

	@Test
	public void listProducts() throws Exception {
		mockMvc.perform( get( PRODUCT_URL ) )
				.andExpect( status().isOk() ).andExpect( content().contentType( contentType ) )
				.andExpect( jsonPath( "$.results", Matchers.hasSize( 2 ) ) )
				.andExpect( jsonPath( "$.results[0].id", Matchers.is( TEST_PRODUCT_1.getProductKey() ) ) )
				.andExpect( jsonPath( "$.results[0].title", Matchers.is( TEST_PRODUCT_1.getTitle() ) ) )
				.andExpect( jsonPath( "$.results[0].summary", Matchers.is( TEST_PRODUCT_1.getSummary() ) ) )
				.andExpect( jsonPath( "$.results[0].description", Matchers.is( TEST_PRODUCT_1.getDescription() ) ) )
				.andExpect( jsonPath( "$.results[1].id", Matchers.is( TEST_PRODUCT_2.getProductKey() ) ) )
				.andExpect( jsonPath( "$.results[1].title", Matchers.is( TEST_PRODUCT_2.getTitle() ) ) )
				.andExpect( jsonPath( "$.results[1].summary", Matchers.is( TEST_PRODUCT_2.getSummary() ) ) )
				.andExpect( jsonPath( "$.results[1].description", Matchers.is( TEST_PRODUCT_2.getDescription() ) ) );
	}

	@Test
	public void productNotFound() throws Exception {
		mockMvc.perform( get( PRODUCT_URL + "/empty" ) ).andExpect( status().isNotFound() );
	}

	@Before
	public void setup() throws Exception {
		super.setup();

		productRepository.save( TEST_PRODUCT_1 );
		productRepository.save( TEST_PRODUCT_2 );
	}
}