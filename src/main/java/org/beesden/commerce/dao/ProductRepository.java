package org.beesden.commerce.dao;

import org.beesden.commerce.model.dto.ProductDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductDTO, Long> {

	void deleteByProductKey( String productKey );

	ProductDTO getOneByProductKey( String productKey );

}