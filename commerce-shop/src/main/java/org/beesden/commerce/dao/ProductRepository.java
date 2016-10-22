package org.beesden.commerce.dao;

import org.beesden.commerce.model.ProductDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductDTO, Long> {

	void deleteByProductKey( String productKey );

	ProductDTO findOneByProductKey( String productKey );

}