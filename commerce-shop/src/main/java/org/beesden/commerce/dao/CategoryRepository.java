package org.beesden.commerce.dao;

import org.beesden.commerce.model.CategoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryDTO, Long> {

	void deleteByCategoryId( String categoryId );

	CategoryDTO getOneByCategoryId( String categoryId );

}