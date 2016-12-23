package org.beesden.commerce.content.dao;

import org.beesden.commerce.content.domain.CategoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryDTO, Long> {

	void deleteByCategoryId( String categoryId );

	CategoryDTO getOneByCategoryId( String categoryId );

}