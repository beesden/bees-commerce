package org.beesden.commerce.dao;

import org.beesden.commerce.model.entity.Product;

public interface ProductDAO {

	public Product getProductById(String id);
}