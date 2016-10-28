package org.beesden.commerce.catalogue.dao;

import org.beesden.commerce.catalogue.domain.ProductDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductDTO, Long> {

	void deleteByProductKey( String productKey );

	ProductDTO findOneByProductKey( String productKey );

}