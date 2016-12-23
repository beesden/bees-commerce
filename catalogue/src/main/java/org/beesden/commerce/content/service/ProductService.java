package org.beesden.commerce.content.service;

import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.PagedResponse;
import org.beesden.commerce.content.model.Product;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface ProductService {

	void createProduct(@Valid @RequestBody Product product);

	void deleteProduct(@PathVariable String productKey);

	Product getProduct(@PathVariable String productKey);

	PagedResponse<Product> listProducts(@Valid PagedRequest pagination);

	void indexProducts();

	void updateProduct(@PathVariable String productKey, @Valid @RequestBody Product product);

}