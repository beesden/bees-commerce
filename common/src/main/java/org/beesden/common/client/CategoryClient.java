package org.beesden.common.client;

import org.beesden.common.model.Category;
import org.beesden.common.model.PagedRequest;
import org.beesden.common.model.PagedResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping( "/categories" )
public interface CategoryClient {

	@RequestMapping( method = RequestMethod.POST )
	String createCategory( @Valid @RequestBody Category category );

	@RequestMapping( value = "/{categoryId}", method = RequestMethod.DELETE )
	void deleteCategory( @PathVariable String categoryId );

	@RequestMapping( value = "/{categoryId}", method = RequestMethod.GET )
	Category getCategory( @PathVariable String categoryId );

	@RequestMapping( method = RequestMethod.GET )
	PagedResponse<Category> listCategories( PagedRequest pagination );

	@RequestMapping( value = "/{categoryId}", method = RequestMethod.PUT )
	String updateCategory( @PathVariable String categoryId, @Valid @RequestBody Category category );

}