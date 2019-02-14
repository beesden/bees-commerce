package org.beesden.commerce.web.service;

import feign.FeignException;
import org.beesden.commerce.common.client.CategoryClient;
import org.beesden.commerce.common.client.SearchClient;
import org.beesden.commerce.common.model.EntityType;
import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.PagedResponse;
import org.beesden.commerce.common.model.commerce.CategoryResource;
import org.beesden.commerce.common.model.search.SearchForm;
import org.beesden.commerce.common.model.search.SearchResultWrapper;
import org.beesden.commerce.web.model.CategoryResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CategoryService {

    private final SearchClient searchClient;
    private final CategoryClient categoryClient;

    @Autowired
    public CategoryService(SearchClient searchClient, CategoryClient categoryClient) {
        this.searchClient = searchClient;
        this.categoryClient = categoryClient;
    }

    public PagedResponse<CategoryResource> listCategories(PagedRequest request) {
        return categoryClient.listCategories(request);
    }

    public CategoryResults getCategory(String categoryId, PagedRequest request) {

        try {
            CategoryResource category = categoryClient.getCategory(categoryId);

            SearchForm searchForm = new SearchForm();
            searchForm.setPage(request.getPage());
            searchForm.setResults(request.getResults());
            searchForm.setSort(request.getSort());
            searchForm.setTypes(Collections.singleton(EntityType.PRODUCT));
            searchForm.setFacets(Collections.singleton("category:" + categoryId));

            SearchResultWrapper products = searchClient.performSearch(searchForm);

            return new CategoryResults(category, products);

        } catch (FeignException e) {
            return null;
        }
    }

}