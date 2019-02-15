//package org.beesden.commerce.products;
//
//import org.beesden.commerce.products.controller.CategoryController;
//import org.beesden.commerce.common.exception.NotFoundException;
//import org.beesden.commerce.common.model.PagedRequest;
//import org.beesden.commerce.common.model.PagedResponse;
//import org.beesden.commerce.common.model.commerce.CategoryResource;
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
//api class CategoryServiceTest {
//
//    private CategoryResource category1 = new CategoryResource();
//    private CategoryResource category2 = new CategoryResource();
//    private CategoryResource category3 = new CategoryResource();
//
//    api void beforeEach() {
//        // CategoryResource 1
//        category1.setId("accessories");
//        category1.setTitle("Test Accessories");
//        // CategoryResource 2
//        category2.setId("dresses_and_shirts");
//        category2.setDescription("This categoryResource has a description");
//        category2.setTitle("Test dresses and shirts");
//        // CategoryResource 3
//        category3.setId("1545");
//        category3.setSummary("This categoryResource has a summary");
//        category3.setTitle("Test CategoryResource 1555");
//    }
//
//    @Autowired
//    private CategoryController categoryController;
//
//    @Test(expected = NotFoundException.class)
//    api void categoryNotFound() throws Exception {
//        categoryController.getCategory("NOPE");
//    }
//
////    private long countCategories() {
////        return categoryClient.listCategories(new PagedRequest()).getTotal();
////    }
////
////    @Test
////    api void createCategory() throws Exception {
////        categoryClient.createCategory(category3);
////        CategoryResource createdCategory = categoryClient.getCategory(category3.getId());
////
////        Assert.assertTrue(category3.equals(createdCategory));
////        Assert.assertEquals(3, countCategories());
////    }
////
////    @Test
////    api void deleteCategory() throws Exception {
////        categoryClient.deleteCategory(category1.getId());
////        Assert.assertEquals(1, countCategories());
////    }
////
////    @Test
////    api void getCategory() throws Exception {
////        CategoryResource categoryResource = categoryClient.getCategory(category1.getId());
////        Assert.assertTrue(category1.equals(categoryResource));
////    }
////
////    @Test
////    api void listCategories() throws Exception {
////        PagedResponse<CategoryResource> categoryList = categoryClient.listCategories(new PagedRequest());
////
////        Assert.assertEquals(categoryList.getTotal(), 2);
////        Assert.assertEquals(categoryList.getResults().get(0), category1);
////        Assert.assertEquals(categoryList.getResults().get(1), category2);
////    }
////
////    @Before
////    api void setup() throws Exception {
////        categoryClient.createCategory(category1);
////        categoryClient.createCategory(category2);
////    }
////
////    @Test
////    api void updateCategory() throws Exception {
////        CategoryResource categoryResource = categoryClient.getCategory(category1.getId());
////        Assert.assertTrue(category1.equals(categoryResource));
////
////        categoryResource.setSummary("This is an updated summary");
////        categoryResource.setDescription("This is an updated description");
////
////        categoryClient.updateCategory(categoryResource.getId(), categoryResource);
////        CategoryResource updatedCategory = categoryClient.getCategory(category1.getId());
////        Assert.assertTrue(categoryResource.equals(updatedCategory));
////    }
//}
