package org.beesden.commerce.api.controller;

import org.beesden.commerce.common.Utils;
import org.beesden.commerce.common.client.CategoryClient;
import org.beesden.commerce.common.client.ProductClient;
import org.beesden.commerce.common.client.SearchClient;
import org.beesden.commerce.common.model.EntityReference;
import org.beesden.commerce.common.model.EntityType;
import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.commerce.Category;
import org.beesden.commerce.common.model.commerce.Product;
import org.beesden.commerce.common.model.search.SearchDocument;
import org.beesden.commerce.common.model.search.SearchForm;
import org.beesden.commerce.common.model.search.SearchResultWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;

@RestController
@RequestMapping(path = "/category")
public class CategoryController {

    @Autowired
    SearchClient searchClient;

    @Autowired
    CategoryClient categoryClient;

    @RequestMapping(path = "/{categoryId}", method = RequestMethod.GET)
    public Category getCategory(@PathVariable String categoryId) {
        return categoryClient.getCategory(categoryId);
    }

    @RequestMapping(path = "/{categoryId}/products", method = RequestMethod.GET)
    public SearchResultWrapper getCategory(@PathVariable String categoryId, @Valid PagedRequest request) {

        SearchForm searchForm = new SearchForm();
        searchForm.setPage(request.getPage());
        searchForm.setResults(request.getResults());
        searchForm.setSort(request.getSort());
        searchForm.setTypes(Collections.singleton(EntityType.PRODUCT));
        searchForm.setFacets(Collections.singleton("category:" + categoryId));

        return searchClient.performSearch(searchForm);
    }

}