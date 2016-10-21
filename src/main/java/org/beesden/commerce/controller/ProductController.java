package org.beesden.commerce.controller;

import lombok.extern.slf4j.Slf4j;
import org.beesden.commerce.model.api.PagedRequest;
import org.beesden.commerce.model.api.PagedResponse;
import org.beesden.commerce.model.api.Product;
import org.beesden.commerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping( "/products" )
@Slf4j
public class ProductController {

	@Autowired
	private ProductService productService;

	@RequestMapping( method = RequestMethod.PUT )
	@ResponseStatus( HttpStatus.CREATED )
	public Product createProduct( @Valid @RequestBody Product product ) {
		return productService.createProduct( product );
	}

	@RequestMapping( value = "/{productKey}", method = RequestMethod.DELETE )
	@ResponseStatus( HttpStatus.NO_CONTENT )
	public void deleteProduct( @PathVariable String productKey ) {
		productService.deleteProduct( productKey );
	}

	@RequestMapping( value = "/{productKey}", method = RequestMethod.GET )
	public Product getProduct( @PathVariable String productKey ) {
		return productService.getProduct( productKey );
	}

	@RequestMapping( method = RequestMethod.GET )
	public PagedResponse<Product> listProducts( PagedRequest pagination ) {
		return productService.listProducts( pagination );
	}

	@RequestMapping( value = "/{productKey}", method = RequestMethod.PUT )
	@ResponseStatus( HttpStatus.NO_CONTENT )
	public void updateProduct( @PathVariable String productKey, @Valid @RequestBody Product product ) {
		productService.updateProduct( productKey, product );
	}

}