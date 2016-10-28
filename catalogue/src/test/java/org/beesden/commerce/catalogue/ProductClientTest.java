package org.beesden.commerce.catalogue;

import org.beesden.common.client.ProductClient;
import org.beesden.common.exception.NotFoundException;
import org.beesden.common.model.PagedRequest;
import org.beesden.common.model.PagedResponse;
import org.beesden.common.model.Product;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith( SpringRunner.class )
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
@ActiveProfiles( "test" )
@Transactional
public class ProductClientTest {

	private static final Product TEST_PRODUCT_1 = new Product();
	private static final Product TEST_PRODUCT_2 = new Product();
	private static final Product TEST_PRODUCT_3 = new Product();

	static {
		// Product 1
		TEST_PRODUCT_1.setId( "p3556" );
		TEST_PRODUCT_1.setTitle( "Test Product 1555" );
		// Product 2
		TEST_PRODUCT_2.setId( "p2885" );
		TEST_PRODUCT_2.setDescription( "This product has a description" );
		TEST_PRODUCT_2.setTitle( "Test Product 1555" );
		// Product 3
		TEST_PRODUCT_3.setId( "p1555" );
		TEST_PRODUCT_3.setSummary( "This product has a summary" );
		TEST_PRODUCT_3.setTitle( "Test Product 1555" );
	}

	@Autowired
	private ProductClient productClient;

	private long countProducts() {
		return productClient.listProducts( new PagedRequest() ).getTotal();
	}

	@Test
	public void createProduct() throws Exception {
		productClient.createProduct( TEST_PRODUCT_3 );
		Product createdProduct = productClient.getProduct( TEST_PRODUCT_3.getId() );

		Assert.assertTrue( TEST_PRODUCT_3.equals( createdProduct ) );
		Assert.assertEquals( 3, countProducts() );
	}

	@Test
	public void deleteProduct() throws Exception {
		productClient.deleteProduct( TEST_PRODUCT_1.getId() );
		Assert.assertEquals( 1, countProducts() );
	}

	@Test
	public void getProduct() throws Exception {
		Product product = productClient.getProduct( TEST_PRODUCT_1.getId() );
		Assert.assertTrue( TEST_PRODUCT_1.equals( product ) );
	}

	@Test
	public void listProducts() throws Exception {
		PagedResponse<Product> productList = productClient.listProducts( new PagedRequest() );

		Assert.assertEquals( productList.getTotal(), 2 );
		Assert.assertEquals( productList.getResults().get( 0 ), TEST_PRODUCT_1 );
		Assert.assertEquals( productList.getResults().get( 1 ), TEST_PRODUCT_2 );
	}

	@Test( expected = NotFoundException.class )
	public void productNotFound() throws Exception {
		productClient.getProduct( "NOPE" );
	}

	@Before
	public void setup() throws Exception {
		productClient.createProduct( TEST_PRODUCT_1 );
		productClient.createProduct( TEST_PRODUCT_2 );
	}

	@Test
	public void updateProduct() throws Exception {
		Product product = productClient.getProduct( TEST_PRODUCT_1.getId() );
		Assert.assertTrue( TEST_PRODUCT_1.equals( product ) );

		product.setSummary( "This is an updated summary" );
		product.setDescription( "This is an updated description" );

		productClient.updateProduct( product.getId(), product );
		Product updatedProduct = productClient.getProduct( TEST_PRODUCT_1.getId() );
		Assert.assertTrue( product.equals( updatedProduct ) );
	}
}
