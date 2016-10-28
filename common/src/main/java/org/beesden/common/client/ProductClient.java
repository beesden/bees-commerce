package org.beesden.common.client;

import org.beesden.common.model.PagedRequest;
import org.beesden.common.model.PagedResponse;
import org.beesden.common.model.Product;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping( "/products" )
public interface ProductClient {

	@RequestMapping( method = RequestMethod.POST )
	void createProduct( @Valid @RequestBody Product product );

	@RequestMapping( value = "/{productId}", method = RequestMethod.DELETE )
	void deleteProduct( @PathVariable String productId );

	@RequestMapping( value = "/{productId}", method = RequestMethod.GET )
	Product getProduct( @PathVariable String productId );

	@RequestMapping( method = RequestMethod.GET )
	PagedResponse<Product> listProducts( PagedRequest pagination );

	@RequestMapping( value = "/{productId}", method = RequestMethod.PUT )
	void updateProduct( @PathVariable String productId, @Valid @RequestBody Product product );

}