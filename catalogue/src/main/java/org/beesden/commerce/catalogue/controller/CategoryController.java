package org.beesden.commerce.catalogue.controller;

import org.beesden.commerce.catalogue.service.CategoryService;
import org.beesden.commerce.common.model.Category;
import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.PagedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {

	private CategoryService categoryService;

	@Autowired
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String createCategory(@Valid @RequestBody Category category) {
		return categoryService.createCategory(category);
	}

	@RequestMapping(value = "/{categoryId}", method = RequestMethod.DELETE)
	public void deleteCategory(@PathVariable String categoryId) {
		categoryService.deleteCategory(categoryId);
	}

	@RequestMapping(value = "/{categoryId}", method = RequestMethod.GET)
	public Category getCategory(@PathVariable String categoryId) {
		return categoryService.getCategory(categoryId);
	}

	@RequestMapping(method = RequestMethod.GET)
	public PagedResponse<Category> listCategories(PagedRequest pagination) {
		return categoryService.listCategories(pagination);
	}

	@RequestMapping(value = "/{categoryId}", method = RequestMethod.PUT)
	public String updateCategory(@PathVariable String categoryId, @Valid @RequestBody Category category) {
		return categoryService.updateCategory(categoryId, category);
	}

}