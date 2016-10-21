package org.beesden.commerce.service;

import org.beesden.commerce.model.api.PagedRequest;
import org.beesden.commerce.model.api.PagedResponse;
import org.beesden.commerce.model.api.Product;

public interface ProductService {

	Product createProduct( Product product );

	void deleteProduct( String productKey );

	Product getProduct( String productKey );

	PagedResponse<Product> listProducts( PagedRequest paginationForm );

	Product updateProduct( String productKey, Product product );

}