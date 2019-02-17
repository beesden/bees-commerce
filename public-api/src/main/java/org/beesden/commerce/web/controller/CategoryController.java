package org.beesden.commerce.web.controller;

import org.beesden.commerce.common.client.CategoryClient;
import org.beesden.commerce.common.client.SearchClient;
import org.beesden.commerce.common.model.SearchRequest;
import org.beesden.commerce.common.model.resource.CategoryResource;
import org.beesden.commerce.web.service.CategoryService;
import org.beesden.commerce.web.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/categories")
public class CategoryController {

    private final Session session;
    private final CategoryClient categoryClient;
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(Session session, CategoryClient categoryClient, CategoryService categoryService) {
        this.session = session;
        this.categoryClient = categoryClient;
        this.categoryService = categoryService;
    }

    @RequestMapping(path = "/{categoryId}", method = RequestMethod.GET)
    public ModelAndView viewCategory(@PathVariable String categoryId, @Valid SearchRequest request) {

        ModelAndView model = new ModelAndView();
        CategoryResource category = categoryClient.getCategory(categoryId);

        if (category != null) {
            model.addObject("session", session);
            model.addObject("request", request);
            model.addObject("category", category);
            model.addObject("products", categoryService.getProducts(categoryId, request));
            model.setViewName("category/view");
        } else {
            model.setViewName("category/empty");
        }

        return model;

    }

}