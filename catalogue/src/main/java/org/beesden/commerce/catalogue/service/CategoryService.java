package org.beesden.commerce.catalogue.service;

import org.beesden.commerce.common.model.Category;
import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.PagedResponse;

public interface CategoryService {

	String createCategory(Category category);

	void deleteCategory(String categoryId);

	Category getCategory(String categoryId);

	PagedResponse<Category> listCategories(PagedRequest pagination);

	String updateCategory(String categoryId, Category category);
}