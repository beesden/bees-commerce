package org.beesden.commerce.service;

import org.beesden.commerce.model.api.PagedRequest;
import org.beesden.commerce.model.api.PagedResponse;
import org.beesden.commerce.model.api.Product;

public interface ProductService {

	Product getProduct( String productKey );

	void deleteProduct( String productKey );

	Product createProduct( Product product );

	Product updateProduct( String productKey, Product product );

	PagedResponse<Product> listProducts( PagedRequest paginationForm );

}