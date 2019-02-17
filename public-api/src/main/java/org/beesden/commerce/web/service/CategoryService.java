package org.beesden.commerce.web.service;

import feign.FeignException;
import org.beesden.commerce.common.client.CategoryClient;
import org.beesden.commerce.common.client.SearchClient;
import org.beesden.commerce.common.model.EntityType;
import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.PagedResponse;
import org.beesden.commerce.common.model.resource.CategoryResource;
import org.beesden.commerce.common.model.SearchRequest;
import org.beesden.commerce.common.model.resource.SearchResource;
import org.beesden.commerce.web.model.CategoryResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

    public CategoryResults getCategory(String categoryId, SearchRequest request) {

        CategoryResource category;
        SearchResource products;

        try {
            category = categoryClient.getCategory(categoryId);
        } catch (FeignException e) {
            return null;
        }

        try {
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.setPage(request.getPage());
            searchRequest.setResults(request.getResults());
            searchRequest.setSort(request.getSort());
            searchRequest.setTypes(Collections.singleton(EntityType.PRODUCT));

            Set<String> facets = request.getFacets();
            if (facets == null) {
                facets = new HashSet<>();
            }
            facets.add("category:" + categoryId);

            searchRequest.setFacets(facets);

            products = searchClient.performSearch(searchRequest);

        } catch (FeignException e) {
            products = new SearchResource();
        }

        return new CategoryResults(category, products);
    }

}