package org.beesden.commerce.controller;

import org.beesden.commerce.service.CategoryService;
import org.beesden.common.model.PagedRequest;
import org.beesden.common.model.PagedResponse;
import org.beesden.common.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping( "/categories" )
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@RequestMapping( method = RequestMethod.POST )
	@ResponseStatus( HttpStatus.CREATED )
	public Category createCategory( @Valid @RequestBody Category category ) {
		return categoryService.createCategory( category );
	}

	@RequestMapping( value = "/{categoryId}", method = RequestMethod.DELETE )
	@ResponseStatus( HttpStatus.NO_CONTENT )
	public void deleteCategory( @PathVariable String categoryId ) {
		categoryService.deleteCategory( categoryId );
	}

	@RequestMapping( value = "/{categoryId}", method = RequestMethod.GET )
	public Category getCategory( @PathVariable String categoryId ) {
		return categoryService.getCategory( categoryId );
	}

	@RequestMapping( method = RequestMethod.GET )
	public PagedResponse<Category> listCategories( PagedRequest pagination ) {
		return categoryService.listCategories( pagination );
	}

	@RequestMapping( value = "/{categoryId}", method = RequestMethod.PUT )
	@ResponseStatus( HttpStatus.NO_CONTENT )
	public void updateCategory( @PathVariable String categoryId, @Valid @RequestBody Category category ) {
		categoryService.updateCategory( categoryId, category );
	}

}