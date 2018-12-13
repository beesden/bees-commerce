package org.beesden.commerce.api.controller;

import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.PagedResponse;
import org.beesden.commerce.common.model.commerce.Category;
import org.beesden.commerce.web.model.APICategory;
import org.beesden.commerce.web.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/category")
public class CategoryAPIController {

    @Autowired
    CategoryService categoryService;

    @RequestMapping(method = RequestMethod.GET)
    public PagedResponse<Category> getCategories(@Valid PagedRequest request) {
        return categoryService.listCategories(request);
    }

    @RequestMapping(path = "/{categoryId}", method = RequestMethod.GET)
    public APICategory getCategory(@PathVariable String categoryId, @Valid PagedRequest request) {
        return categoryService.getCategory(categoryId, request);
    }

}