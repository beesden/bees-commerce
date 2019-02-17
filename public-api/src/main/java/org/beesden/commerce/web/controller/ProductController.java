package org.beesden.commerce.web.controller;

import org.beesden.commerce.common.client.ProductClient;
import org.beesden.commerce.common.model.resource.ProductResource;
import org.beesden.commerce.common.model.SearchRequest;
import org.beesden.commerce.web.model.CategoryResults;
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
    private final ProductClient productClient;

    @Autowired
    public ProductController(CategoryService categoryService, ProductClient productClient) {
        this.categoryService = categoryService;
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

        ModelAndView model = new ModelAndView();
        ProductResource product = productClient.getProduct(productKey);

        if (product != null) {
            CategoryResults category = categoryService.getCategory(categoryId, new SearchRequest());
            model.addObject("product", product);
            model.addObject("category", category.getCategory());
            model.addObject("relatedProducts", category.getProducts());
            model.setViewName("product/details");
        } else {
            model.setViewName("product/empty");
        }

        return model;

    }
}