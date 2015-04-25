package org.beesden.commerce.controller.rest;

import org.apache.log4j.Logger;
import org.beesden.commerce.model.PaginationForm;
import org.beesden.commerce.model.RestResponseWrapper;
import org.beesden.commerce.model.entity.Product;
import org.beesden.commerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

	static final Logger logger = Logger.getLogger(ProductController.class);

	@Autowired
	private ProductService productService;

	@RequestMapping(value = "/{productId}", method = RequestMethod.GET)
	public RestResponseWrapper fetchProductDetails(@PathVariable String productId, @ModelAttribute PaginationForm pagination) {

		logger.info("Requesting product details");

		Product product = productService.getProductById(productId);
		RestResponseWrapper wrapper = new RestResponseWrapper();
		wrapper.addContent("product", product);

		return wrapper;
	}

}