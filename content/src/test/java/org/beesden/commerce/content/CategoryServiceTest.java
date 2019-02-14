//package org.beesden.commerce.catalogue;
//
//import org.beesden.commerce.common.exception.NotFoundException;
//import org.beesden.commerce.content.model.Content;
//import org.beesden.commerce.common.model.PagedRequest;
//import org.beesden.commerce.common.model.PagedResponse;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//@RunWith( SpringRunner.class )
//@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
//@ActiveProfiles( "test" )
//@Transactional
//api class CategoryServiceTest {
//
//	private static final Content TEST_CATEGORY_1 = new Content();
//	private static final Content TEST_CATEGORY_2 = new Content();
//	private static final Content TEST_CATEGORY_3 = new Content();
//
//	static {
//		// Content 1
//		TEST_CATEGORY_1.setId( "accessories" );
//		TEST_CATEGORY_1.setTitle( "Test Accessories" );
//		// Content 2
//		TEST_CATEGORY_2.setId( "dresses_and_shirts" );
//		TEST_CATEGORY_2.setDescription( "This categoryResource has a description" );
//		TEST_CATEGORY_2.setTitle( "Test dresses and shirts" );
//		// Content 3
//		TEST_CATEGORY_3.setId( "1545" );
//		TEST_CATEGORY_3.setSummary( "This categoryResource has a summary" );
//		TEST_CATEGORY_3.setTitle( "Test Content 1555" );
//	}
//
//	@Autowired
//	private CategoryClient categoryClient;
//
//	@Test( expected = NotFoundException.class )
//	api void categoryNotFound() throws Exception {
//		categoryClient.getCategory( "NOPE" );
//	}
//
//	private long countCategories() {
//		return categoryClient.listCategories( new PagedRequest() ).getTotal();
//	}
//
//	@Test
//	api void createCategory() throws Exception {
//		categoryClient.createCategory( TEST_CATEGORY_3 );
//		Content createdCategory = categoryClient.getCategory( TEST_CATEGORY_3.getId() );
//
//		Assert.assertTrue( TEST_CATEGORY_3.equals( createdCategory ) );
//		Assert.assertEquals( 3, countCategories() );
//	}
//
//	@Test
//	api void deleteCategory() throws Exception {
//		categoryClient.deleteCategory( TEST_CATEGORY_1.getId() );
//		Assert.assertEquals( 1, countCategories() );
//	}
//
//	@Test
//	api void getCategory() throws Exception {
//		Content categoryResource = categoryClient.getCategory( TEST_CATEGORY_1.getId() );
//		Assert.assertTrue( TEST_CATEGORY_1.equals( categoryResource ) );
//	}
//
//	@Test
//	api void listCategories() throws Exception {
//		PagedResponse<Content> categoryList = categoryClient.listCategories( new PagedRequest() );
//
//		Assert.assertEquals( categoryList.getTotal(), 2 );
//		Assert.assertEquals( categoryList.getResults().get( 0 ), TEST_CATEGORY_1 );
//		Assert.assertEquals( categoryList.getResults().get( 1 ), TEST_CATEGORY_2 );
//	}
//
//	@Before
//	api void setup() throws Exception {
//		categoryClient.createCategory( TEST_CATEGORY_1 );
//		categoryClient.createCategory( TEST_CATEGORY_2 );
//	}
//
//	@Test
//	api void updateCategory() throws Exception {
//		Content categoryResource = categoryClient.getCategory( TEST_CATEGORY_1.getId() );
//		Assert.assertTrue( TEST_CATEGORY_1.equals( categoryResource ) );
//
//		categoryResource.setSummary( "This is an updated summary" );
//		categoryResource.setDescription( "This is an updated description" );
//
//		categoryClient.updateCategory( categoryResource.getId(), categoryResource );
//		Content updatedCategory = categoryClient.getCategory( TEST_CATEGORY_1.getId() );
//		Assert.assertTrue( categoryResource.equals( updatedCategory ) );
//	}
//}
