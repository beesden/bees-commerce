package org.beesden.commerce.catalogue.dao;

import org.beesden.commerce.catalogue.domain.CategoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryDTO, Long> {

	void deleteByCategoryId( String categoryId );

	CategoryDTO findOneByCategoryId( String categoryId );

}