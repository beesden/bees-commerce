package org.beesden.commerce.common.client;

import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.PagedResponse;
import org.beesden.commerce.common.model.Product;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@FeignClient
@RequestMapping( "/products" )
public interface ProductClient {

	@RequestMapping( method = RequestMethod.POST )
	void createProduct( @Valid @RequestBody Product product );

	@RequestMapping( value = "/{productId}", method = RequestMethod.DELETE )
	void deleteProduct( @PathVariable String productId );

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	void indexProducts();

	@RequestMapping( value = "/{productId}", method = RequestMethod.GET )
	Product getProduct( @PathVariable String productId );

	@RequestMapping( method = RequestMethod.GET )
	PagedResponse<Product> listProducts(@Valid PagedRequest pagination);

	@RequestMapping( value = "/{productId}", method = RequestMethod.PUT )
	void updateProduct( @PathVariable String productId, @Valid @RequestBody Product product );

}