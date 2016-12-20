package org.beesden.commerce.common.client;

import org.beesden.commerce.common.model.Category;
import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.PagedResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@FeignClient
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