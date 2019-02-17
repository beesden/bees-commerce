package org.beesden.commerce.web.controller;

import org.beesden.commerce.common.client.CategoryClient;
import org.beesden.commerce.common.client.ProductClient;
import org.beesden.commerce.common.model.resource.CategoryResource;
import org.beesden.commerce.common.model.resource.ProductResource;
import org.beesden.commerce.common.model.SearchRequest;
import org.beesden.commerce.common.model.resource.SearchResource;
import org.beesden.commerce.web.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ProductController {

    private final CategoryService categoryService;
    private final CategoryClient categoryClient;
    private final ProductClient productClient;

    @Autowired
    public ProductController(CategoryService categoryService, CategoryClient categoryClient, ProductClient productClient) {
        this.categoryService = categoryService;
        this.categoryClient = categoryClient;
        this.productClient = productClient;
    }

    @RequestMapping(path = "/products/{productKey}", method = RequestMethod.GET)
    public ModelAndView viewProduct(@PathVariable String productKey) {

        ModelAndView model = new ModelAndView();
        ProductResource product = productClient.getProduct(productKey);

        if (product != null) {
            model.addObject("product", product);
            model.setViewName("product/details");
        } else {
            model.setViewName("product/empty");
        }

        return model;

    }

    @RequestMapping(path = "/categories/{categoryId}/{productKey}", method = RequestMethod.GET)
    public ModelAndView viewProduct(@PathVariable String categoryId, @PathVariable String productKey) {

        ProductResource product = productClient.getProduct(productKey);

        if (product == null) {
            return new ModelAndView("product/empty");
        }

        ModelAndView model = new ModelAndView("product/details");
        model.addObject("product", product);

        product.getCategories().stream().filter(category -> category.equals(categoryId)).findFirst().ifPresent(category -> {
            CategoryResource currentCategory = categoryClient.getCategory(categoryId);
            SearchResource relatedProducts = categoryService.getProducts(categoryId, new SearchRequest());
            model.addObject("currentCategory", currentCategory);
            model.addObject("relatedProducts", relatedProducts);

        });

        return model;

    }
}