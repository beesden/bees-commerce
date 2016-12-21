package org.beesden.commerce.catalogue.controller;

import org.beesden.commerce.catalogue.service.ProductService;
import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.PagedResponse;
import org.beesden.commerce.common.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Service
@Transactional
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

	@RequestMapping(value = "/{productId}", method = RequestMethod.DELETE)
	public void deleteProduct(@PathVariable String productKey) {
		productService.deleteProduct(productKey);
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public Product getProduct(@PathVariable String productKey) {
		return productService.getProduct(productKey);
	}

	@RequestMapping(value = "/{productId}", method = RequestMethod.GET)
	public PagedResponse<Product> listProducts(@Valid PagedRequest pagination) {
		return productService.listProducts(pagination);
	}

	@RequestMapping(method = RequestMethod.GET)
	public void indexProducts() {
		productService.indexProducts();
	}

	@RequestMapping(value = "/{productId}", method = RequestMethod.PUT)
	public void updateProduct(@PathVariable String productKey, @Valid @RequestBody Product product) {
		productService.updateProduct(productKey, product);
	}

}