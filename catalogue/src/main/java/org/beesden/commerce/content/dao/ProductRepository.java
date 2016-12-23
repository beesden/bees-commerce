package org.beesden.commerce.content.dao;

import org.beesden.commerce.content.domain.ProductDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductDTO, Long> {

	void deleteByProductKey( String productKey );

	ProductDTO findOneByProductKey( String productKey );

}