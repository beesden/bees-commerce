package org.beesden.commerce.service;

import org.beesden.common.model.PagedRequest;
import org.beesden.common.model.PagedResponse;
import org.beesden.common.model.Product;

public interface ProductService {

	Product createProduct( Product product );

	void deleteProduct( String productKey );

	Product getProduct( String productKey );

	PagedResponse<Product> listProducts( PagedRequest paginationForm );

	Product updateProduct( String productKey, Product product );

}