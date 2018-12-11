//package org.beesden.commerce.catalogue;
//
//import org.beesden.commerce.catalogue.controller.CategoryController;
//import org.beesden.commerce.common.exception.NotFoundException;
//import org.beesden.commerce.common.model.PagedRequest;
//import org.beesden.commerce.common.model.PagedResponse;
//import org.beesden.commerce.common.model.commerce.Category;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("test")
//@Transactional
//public class CategoryServiceTest {
//
//    private Category category1 = new Category();
//    private Category category2 = new Category();
//    private Category category3 = new Category();
//
//    public void beforeEach() {
//        // Category 1
//        category1.setId("accessories");
//        category1.setTitle("Test Accessories");
//        // Category 2
//        category2.setId("dresses_and_shirts");
//        category2.setDescription("This category has a description");
//        category2.setTitle("Test dresses and shirts");
//        // Category 3
//        category3.setId("1545");
//        category3.setSummary("This category has a summary");
//        category3.setTitle("Test Category 1555");
//    }
//
//    @Autowired
//    private CategoryController categoryController;
//
//    @Test(expected = NotFoundException.class)
//    public void categoryNotFound() throws Exception {
//        categoryController.getCategory("NOPE");
//    }
//
////    private long countCategories() {
////        return categoryClient.listCategories(new PagedRequest()).getTotal();
////    }
////
////    @Test
////    public void createCategory() throws Exception {
////        categoryClient.createCategory(category3);
////        Category createdCategory = categoryClient.getCategory(category3.getId());
////
////        Assert.assertTrue(category3.equals(createdCategory));
////        Assert.assertEquals(3, countCategories());
////    }
////
////    @Test
////    public void deleteCategory() throws Exception {
////        categoryClient.deleteCategory(category1.getId());
////        Assert.assertEquals(1, countCategories());
////    }
////
////    @Test
////    public void getCategory() throws Exception {
////        Category category = categoryClient.getCategory(category1.getId());
////        Assert.assertTrue(category1.equals(category));
////    }
////
////    @Test
////    public void listCategories() throws Exception {
////        PagedResponse<Category> categoryList = categoryClient.listCategories(new PagedRequest());
////
////        Assert.assertEquals(categoryList.getTotal(), 2);
////        Assert.assertEquals(categoryList.getResults().get(0), category1);
////        Assert.assertEquals(categoryList.getResults().get(1), category2);
////    }
////
////    @Before
////    public void setup() throws Exception {
////        categoryClient.createCategory(category1);
////        categoryClient.createCategory(category2);
////    }
////
////    @Test
////    public void updateCategory() throws Exception {
////        Category category = categoryClient.getCategory(category1.getId());
////        Assert.assertTrue(category1.equals(category));
////
////        category.setSummary("This is an updated summary");
////        category.setDescription("This is an updated description");
////
////        categoryClient.updateCategory(category.getId(), category);
////        Category updatedCategory = categoryClient.getCategory(category1.getId());
////        Assert.assertTrue(category.equals(updatedCategory));
////    }
//}
