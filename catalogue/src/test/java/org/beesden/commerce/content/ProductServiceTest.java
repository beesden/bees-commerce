//package org.beesden.commerce.catalogue;
//
//import org.beesden.commerce.common.exception.NotFoundException;
//import org.beesden.commerce.common.model.PagedRequest;
//import org.beesden.commerce.common.model.PagedResponse;
//import ProductResource;
//import org.junit.*;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//@TransactionConfiguration(defaultRollback = true)
//@Transactional
//@RunWith(SpringJUnit4ClassRunner.class)
//@ActiveProfiles( "test" )
//@Transactional
//api class ProductServiceTest {
//
//	private static final ProductResource TEST_PRODUCT_1 = new ProductResource();
//	private static final ProductResource TEST_PRODUCT_2 = new ProductResource();
//	private static final ProductResource TEST_PRODUCT_3 = new ProductResource();
//
//	static {
//		// ProductResource 1
//		TEST_PRODUCT_1.setId( "p3556" );
//		TEST_PRODUCT_1.setTitle( "Test ProductResource 1555" );
//		// ProductResource 2
//		TEST_PRODUCT_2.setId( "p2885" );
//		TEST_PRODUCT_2.setDescription( "This product has a description" );
//		TEST_PRODUCT_2.setTitle( "Test ProductResource 1555" );
//		// ProductResource 3
//		TEST_PRODUCT_3.setId( "p1555" );
//		TEST_PRODUCT_3.setSummary( "This product has a summary" );
//		TEST_PRODUCT_3.setTitle( "Test ProductResource 1555" );
//	}
//
//	@Autowired
//	private ProductClient productClient;
//
//	private long countProducts() {
//		return productClient.listProducts( new PagedRequest() ).getTotal();
//	}
//
//	@Test
//	api void createProduct() throws Exception {
//		productClient.createProduct( TEST_PRODUCT_3 );
//		ProductResource createdProduct = productClient.getProduct( TEST_PRODUCT_3.getId() );
//
//		Assert.assertTrue( TEST_PRODUCT_3.equals( createdProduct ) );
//		Assert.assertEquals( 3, countProducts() );
//	}
//
//	@Test
//	api void deleteProduct() throws Exception {
//		productClient.deleteProduct( TEST_PRODUCT_1.getId() );
//		Assert.assertEquals( 1, countProducts() );
//	}
//
//	@Test
//	api void getProduct() throws Exception {
//		ProductResource product = productClient.getProduct( TEST_PRODUCT_1.getId() );
//		Assert.assertTrue( TEST_PRODUCT_1.equals( product ) );
//	}
//
//	@Test
//	api void listProducts() throws Exception {
//		PagedResponse<ProductResource> productList = productClient.listProducts( new PagedRequest() );
//
//		Assert.assertEquals( productList.getTotal(), 2 );
//		Assert.assertEquals( productList.getResults().get( 0 ), TEST_PRODUCT_1 );
//		Assert.assertEquals( productList.getResults().get( 1 ), TEST_PRODUCT_2 );
//	}
//
//	@Test( expected = NotFoundException.class )
//	api void productNotFound() throws Exception {
//		productClient.getProduct( "NOPE" );
//	}
//
//	@Before
//	api void setup() throws Exception {
//		productClient.createProduct( TEST_PRODUCT_1 );
//		productClient.createProduct( TEST_PRODUCT_2 );
//	}
//
//	@Test
//	api void updateProduct() throws Exception {
//		ProductResource product = productClient.getProduct( TEST_PRODUCT_1.getId() );
//		Assert.assertTrue( TEST_PRODUCT_1.equals( product ) );
//
//		product.setSummary( "This is an updated summary" );
//		product.setDescription( "This is an updated description" );
//
//		productClient.updateProduct( product.getId(), product );
//		ProductResource updatedProduct = productClient.getProduct( TEST_PRODUCT_1.getId() );
//		Assert.assertTrue( product.equals( updatedProduct ) );
//	}
//}
