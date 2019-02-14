package org.beesden.commerce.web.api;

import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.PagedResponse;
import org.beesden.commerce.common.model.commerce.CategoryResource;
import org.beesden.commerce.web.model.CategoryResults;
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

    private final CategoryService categoryService;

    @Autowired
    public CategoryAPIController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public PagedResponse<CategoryResource> getCategories(@Valid PagedRequest request) {
        return categoryService.listCategories(request);
    }

    @RequestMapping(path = "/{categoryId}", method = RequestMethod.GET)
    public CategoryResults getCategory(@PathVariable String categoryId, @Valid PagedRequest request) {
        return categoryService.getCategory(categoryId, request);
    }

}