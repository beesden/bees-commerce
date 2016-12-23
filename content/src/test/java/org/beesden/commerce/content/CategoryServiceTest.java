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
//public class CategoryServiceTest {
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
//		TEST_CATEGORY_2.setDescription( "This category has a description" );
//		TEST_CATEGORY_2.setTitle( "Test dresses and shirts" );
//		// Content 3
//		TEST_CATEGORY_3.setId( "1545" );
//		TEST_CATEGORY_3.setSummary( "This category has a summary" );
//		TEST_CATEGORY_3.setTitle( "Test Content 1555" );
//	}
//
//	@Autowired
//	private CategoryClient categoryClient;
//
//	@Test( expected = NotFoundException.class )
//	public void categoryNotFound() throws Exception {
//		categoryClient.getCategory( "NOPE" );
//	}
//
//	private long countCategories() {
//		return categoryClient.listCategories( new PagedRequest() ).getTotal();
//	}
//
//	@Test
//	public void createCategory() throws Exception {
//		categoryClient.createCategory( TEST_CATEGORY_3 );
//		Content createdCategory = categoryClient.getCategory( TEST_CATEGORY_3.getId() );
//
//		Assert.assertTrue( TEST_CATEGORY_3.equals( createdCategory ) );
//		Assert.assertEquals( 3, countCategories() );
//	}
//
//	@Test
//	public void deleteCategory() throws Exception {
//		categoryClient.deleteCategory( TEST_CATEGORY_1.getId() );
//		Assert.assertEquals( 1, countCategories() );
//	}
//
//	@Test
//	public void getCategory() throws Exception {
//		Content category = categoryClient.getCategory( TEST_CATEGORY_1.getId() );
//		Assert.assertTrue( TEST_CATEGORY_1.equals( category ) );
//	}
//
//	@Test
//	public void listCategories() throws Exception {
//		PagedResponse<Content> categoryList = categoryClient.listCategories( new PagedRequest() );
//
//		Assert.assertEquals( categoryList.getTotal(), 2 );
//		Assert.assertEquals( categoryList.getResults().get( 0 ), TEST_CATEGORY_1 );
//		Assert.assertEquals( categoryList.getResults().get( 1 ), TEST_CATEGORY_2 );
//	}
//
//	@Before
//	public void setup() throws Exception {
//		categoryClient.createCategory( TEST_CATEGORY_1 );
//		categoryClient.createCategory( TEST_CATEGORY_2 );
//	}
//
//	@Test
//	public void updateCategory() throws Exception {
//		Content category = categoryClient.getCategory( TEST_CATEGORY_1.getId() );
//		Assert.assertTrue( TEST_CATEGORY_1.equals( category ) );
//
//		category.setSummary( "This is an updated summary" );
//		category.setDescription( "This is an updated description" );
//
//		categoryClient.updateCategory( category.getId(), category );
//		Content updatedCategory = categoryClient.getCategory( TEST_CATEGORY_1.getId() );
//		Assert.assertTrue( category.equals( updatedCategory ) );
//	}
//}
