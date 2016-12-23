package org.beesden.commerce.content.controller;

import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.PagedResponse;
import org.beesden.commerce.content.model.Product;
import org.beesden.commerce.content.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

	private ProductService productService;

	@Autowired
	ProductController(ProductService productService) {
		this.productService = productService;
	}

	@RequestMapping(method = RequestMethod.POST)
	public void createProduct(@Valid @RequestBody Product product) {
		productService.createProduct(product);
	}

	@RequestMapping(value = "/{productKey}", method = RequestMethod.DELETE)
	public void deleteProduct(@PathVariable String productKey) {
		productService.deleteProduct(productKey);
	}

	@RequestMapping(value = "/{productKey}", method = RequestMethod.GET)
	public Product getProduct(@PathVariable String productKey) {
		return productService.getProduct(productKey);
	}

	@RequestMapping(method = RequestMethod.GET)
	public PagedResponse<Product> listProducts(@Valid PagedRequest pagination) {
		return productService.listProducts(pagination);
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public void indexProducts() {
		productService.indexProducts();
	}

	@RequestMapping(value = "/{productKey}", method = RequestMethod.PUT)
	public void updateProduct(@PathVariable String productKey, @Valid @RequestBody Product product) {
		productService.updateProduct(productKey, product);
	}

}