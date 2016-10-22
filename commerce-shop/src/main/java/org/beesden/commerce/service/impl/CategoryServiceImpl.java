package org.beesden.commerce.service.impl;

import org.beesden.commerce.dao.CategoryRepository;
import org.beesden.commerce.dao.ProductRepository;
import org.beesden.commerce.model.CategoryDTO;
import org.beesden.commerce.service.CategoryService;
import org.beesden.common.EntityType;
import org.beesden.common.exception.NotFoundException;
import org.beesden.common.model.Category;
import org.beesden.common.model.PagedRequest;
import org.beesden.common.model.PagedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

	private CategoryRepository categoryRepository;
	private ProductRepository productRepository;

	@Autowired
	public CategoryServiceImpl( CategoryRepository categoryRepository, ProductRepository productRepository ) {
		this.categoryRepository = categoryRepository;
		this.productRepository = productRepository;
	}

	@Override
	public Category createCategory( Category category ) {

		CategoryDTO createdCategory = new CategoryDTO();
		createdCategory.update( category );
		createdCategory.updateTimestamps();
		createdCategory = categoryRepository.save( createdCategory );

		return createdCategory.toCategory();
	}

	@Override
	public void deleteCategory( String categoryId ) {
		categoryRepository.deleteByCategoryId( categoryId );
	}

	@Override
	public Category getCategory( String categoryId ) {

		CategoryDTO category = categoryRepository.getOneByCategoryId( categoryId );
		if ( category == null ) {
			throw new NotFoundException( EntityType.PRODUCT, categoryId );
		}

		return category.toCategory();

	}

	@Override
	public PagedResponse<Category> listCategories( PagedRequest pagination ) {

		Page<CategoryDTO> pagedCategories = categoryRepository.findAll( pagination.toPageable() );
		List<Category> categoryList = pagedCategories.getContent()
				.stream()
				.map( CategoryDTO::toCategory )
				.collect( Collectors.toList() );

		return new PagedResponse<>( categoryList, pagedCategories.getTotalElements() );

	}

	@Override
	public Category updateCategory( String categoryId, Category category ) {

		CategoryDTO updatedCategory = categoryRepository.getOneByCategoryId( categoryId );
		if ( updatedCategory == null ) {
			throw new NotFoundException( EntityType.PRODUCT, categoryId );
		}

		updatedCategory.update( category );
		updatedCategory.updateTimestamps();
		updatedCategory = categoryRepository.save( updatedCategory );

		return updatedCategory.toCategory();
	}

}