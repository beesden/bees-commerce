package org.beesden.commerce.catalogue.controller;

import org.beesden.commerce.catalogue.dao.CategoryRepository;
import org.beesden.commerce.catalogue.domain.Category;
import org.beesden.commerce.common.client.CategoryClient;
import org.beesden.commerce.common.exception.NotFoundException;
import org.beesden.commerce.common.exception.UniqueEntityException;
import org.beesden.commerce.common.model.EntityType;
import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.PagedResponse;
import org.beesden.commerce.common.model.commerce.CategoryResource;
import org.beesden.commerce.common.util.RequestObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/category")
public class CategoryResourceController implements CategoryClient {

    private CategoryRepository categoryRepository;

    @Autowired
    CategoryResourceController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void createCategory(@Valid @RequestBody CategoryResource categoryResource) {

        Category category = categoryRepository.findOneByCategoryId(categoryResource.getId());
        if (category == null) {
            category = new Category();
            category.setCreated(LocalDateTime.now());
            category.setCreatedBy("testuser");
        } else {
            throw new UniqueEntityException(EntityType.CATEGORY, category.getCategoryId());
        }

        category.update(categoryResource);
        categoryRepository.save(category);

    }

    public void deleteCategory(@PathVariable String categoryId) {
        categoryRepository.deleteByCategoryId(categoryId);
    }

    public CategoryResource getCategory(@PathVariable String categoryId) {

        Category category = categoryRepository.findOneByCategoryId(categoryId);
        // todo - abstract
        if (category == null) {
            throw new NotFoundException(EntityType.CATEGORY, categoryId);
        }

        return category.toCategory();

    }

    public PagedResponse<CategoryResource> listCategories(@Valid @RequestObject PagedRequest pagination) {

        Page<Category> categories = categoryRepository.findAll(pagination.toPageable());
        List<CategoryResource> categoryResourceList = categories.getContent()
                .stream()
                .map(Category::toCategory)
                .collect(Collectors.toList());

        return new PagedResponse<>(categoryResourceList, categories.getTotalElements());

    }

    public void updateCategory(@PathVariable String categoryId, @Valid @RequestBody CategoryResource categoryResource) {

        Category category = categoryRepository.findOneByCategoryId(categoryId);
        // todo - abstract
        if (category == null) {
            throw new NotFoundException(EntityType.CATEGORY, categoryId);
        }

        category.update(categoryResource);
        categoryRepository.save(category);

    }

}