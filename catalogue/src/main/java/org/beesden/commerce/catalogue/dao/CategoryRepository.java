package org.beesden.commerce.catalogue.dao;

import org.beesden.commerce.catalogue.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	void deleteByCategoryId( String categoryId );

	Category findOneByCategoryId(String categoryId );

}