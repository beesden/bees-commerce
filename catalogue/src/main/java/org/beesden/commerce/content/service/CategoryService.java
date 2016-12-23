package org.beesden.commerce.content.service;

import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.PagedResponse;
import org.beesden.commerce.content.model.Category;

public interface CategoryService {

	String createCategory(Category category);

	void deleteCategory(String categoryId);

	Category getCategory(String categoryId);

	PagedResponse<Category> listCategories(PagedRequest pagination);

	String updateCategory(String categoryId, Category category);
}