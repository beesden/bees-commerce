package org.beesden.commerce.web.controller;

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
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView viewCategories(@Valid PagedRequest request) {

        ModelAndView model = new ModelAndView("category.list");
        model.addObject("categories", categoryService.listCategories(request));
        return model;

    }

    @RequestMapping(path = "/{categoryId}", method = RequestMethod.GET)
    public ModelAndView viewCategory(@PathVariable String categoryId, @Valid PagedRequest request) {

        ModelAndView model = new ModelAndView("category.view");
        model.addObject("category", categoryService.getCategory(categoryId, request));
        return model;

    }

}