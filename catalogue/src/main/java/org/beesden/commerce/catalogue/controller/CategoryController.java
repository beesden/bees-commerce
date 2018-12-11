package org.beesden.commerce.catalogue.controller;

import org.beesden.commerce.catalogue.dao.CategoryRepository;
import org.beesden.commerce.catalogue.domain.CategoryDTO;
import org.beesden.commerce.common.exception.NotFoundException;
import org.beesden.commerce.common.model.EntityType;
import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.PagedResponse;
import org.beesden.commerce.common.model.commerce.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/category")
public class CategoryController {

	private CategoryRepository categoryRepository;

	@Autowired
	CategoryController(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String createCategory(@Valid @RequestBody Category category) {

		CategoryDTO createdCategory = new CategoryDTO();
		createdCategory.update(category);
		createdCategory.updateTimestamps();

		createdCategory = categoryRepository.save(createdCategory);
		return createdCategory.getCategoryId();

	}

	@RequestMapping(value = "/{categoryKey}", method = RequestMethod.DELETE)
	public void deleteCategory(@PathVariable String categoryKey) {

		categoryRepository.deleteByCategoryId(categoryKey);

	}

	@RequestMapping(value = "/{categoryKey}", method = RequestMethod.GET)
	public Category getCategory(@PathVariable String categoryKey) {

		CategoryDTO category = categoryRepository.findOneByCategoryId(categoryKey);
		// todo - abstract
		if (category == null) {
			throw new NotFoundException(EntityType.CATEGORY, categoryKey);
		}

		return category.toCategory();

	}

	@RequestMapping(method = RequestMethod.GET)
	public PagedResponse<Category> listCategorys(@Valid PagedRequest pagination) {

		Page<CategoryDTO> pagedCategorys = categoryRepository.findAll(pagination.toPageable());
		List<Category> categoryList = pagedCategorys.getContent()
				.stream()
				.map(CategoryDTO::toCategory)
				.collect(Collectors.toList());

		return new PagedResponse<>(categoryList, pagedCategorys.getTotalElements());

	}

	@RequestMapping(value = "/{categoryKey}", method = RequestMethod.PUT)
	public void updateCategory(@PathVariable String categoryKey, @Valid @RequestBody Category category) {

		CategoryDTO target = categoryRepository.findOneByCategoryId(categoryKey);
		// todo - abstract
		if (target == null) {
			throw new NotFoundException(EntityType.CATEGORY, categoryKey);
		}

		target.update(category);
		target.updateTimestamps();
		categoryRepository.save(target);

	}

}