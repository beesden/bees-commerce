package org.beesden.commerce.service.impl;

import org.apache.log4j.Logger;
import org.beesden.commerce.dao.ProductDAO;
import org.beesden.commerce.model.entity.Product;
import org.beesden.commerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

	static final Logger logger = Logger.getLogger(ProductServiceImpl.class);

	@Autowired
	private ProductDAO productDAO;

	@Override
	@Transactional
	public Product getProductById(String id) {
		logger.debug("Fetching product by id: " + id);
		return productDAO.getProductById(id);
	}

}