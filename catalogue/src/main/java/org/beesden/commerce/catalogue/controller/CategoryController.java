package org.beesden.commerce.catalogue.controller;

import org.beesden.commerce.catalogue.dao.CategoryRepository;
import org.beesden.commerce.catalogue.domain.CategoryDTO;
import org.beesden.commerce.catalogue.domain.ProductDTO;
import org.beesden.commerce.common.client.CategoryClient;
import org.beesden.commerce.common.exception.NotFoundException;
import org.beesden.commerce.common.model.EntityType;
import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.PagedResponse;
import org.beesden.commerce.common.model.commerce.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/category")
public class CategoryController implements CategoryClient  {

	private CategoryRepository categoryRepository;

	@Autowired
	CategoryController(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public void createCategory(@Valid @RequestBody Category category) {

		CategoryDTO target = categoryRepository.findOneByCategoryId(category.getId());
		if (target == null) {
			target = new CategoryDTO();
			target.setCreated(LocalDateTime.now());
			target.setCreatedBy("testuser");
		}

		target.update(category);
		categoryRepository.save(target);

	}

	public void deleteCategory(@PathVariable String categoryId) {

		categoryRepository.deleteByCategoryId(categoryId);

	}

	public Category getCategory(@PathVariable String categoryId) {

		CategoryDTO category = categoryRepository.findOneByCategoryId(categoryId);
		// todo - abstract
		if (category == null) {
			throw new NotFoundException(EntityType.CATEGORY, categoryId);
		}

		return category.toCategory();

	}

	public PagedResponse<Category> listCategories(@Valid PagedRequest pagination) {

		Page<CategoryDTO> pagedCategorys = categoryRepository.findAll(pagination.toPageable());
		List<Category> categoryList = pagedCategorys.getContent()
				.stream()
				.map(CategoryDTO::toCategory)
				.collect(Collectors.toList());

		return new PagedResponse<>(categoryList, pagedCategorys.getTotalElements());

	}

	public void updateCategory(@PathVariable String categoryId, @Valid @RequestBody Category category) {

		CategoryDTO target = categoryRepository.findOneByCategoryId(categoryId);
		// todo - abstract
		if (target == null) {
			throw new NotFoundException(EntityType.CATEGORY, categoryId);
		}

		target.update(category);
		categoryRepository.save(target);

	}

}