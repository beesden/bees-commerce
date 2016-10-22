package org.beesden.commerce.service;

import org.beesden.common.model.Category;
import org.beesden.common.model.PagedRequest;
import org.beesden.common.model.PagedResponse;

public interface CategoryService {

	Category createCategory( Category category );

	void deleteCategory( String categoryId );

	Category getCategory( String categoryId );

	PagedResponse<Category> listCategories( PagedRequest paginationForm );

	Category updateCategory( String categoryId, Category category );

}