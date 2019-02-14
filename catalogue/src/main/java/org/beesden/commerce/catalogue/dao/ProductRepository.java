package org.beesden.commerce.catalogue.dao;

import org.beesden.commerce.catalogue.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

	void deleteByProductKey( String productKey );

	Product findOneByProductKey(String productKey );

}