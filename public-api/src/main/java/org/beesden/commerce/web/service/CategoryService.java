package org.beesden.commerce.web.service;

import org.beesden.commerce.common.client.CategoryClient;
import org.beesden.commerce.common.client.SearchClient;
import org.beesden.commerce.common.model.EntityType;
import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.PagedResponse;
import org.beesden.commerce.common.model.commerce.Category;
import org.beesden.commerce.common.model.search.SearchForm;
import org.beesden.commerce.common.model.search.SearchResultWrapper;
import org.beesden.commerce.web.model.APICategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;

@Service
public class CategoryService {

    @Autowired
    SearchClient searchClient;

    @Autowired
    CategoryClient categoryClient;

    public PagedResponse<Category> listCategories(PagedRequest request) {
        return categoryClient.listCategories(request);
    }

    public APICategory getCategory(String categoryId, PagedRequest request) {

        SearchForm searchForm = new SearchForm();
        searchForm.setPage(request.getPage());
        searchForm.setResults(request.getResults());
        searchForm.setSort(request.getSort());
        searchForm.setTypes(Collections.singleton(EntityType.PRODUCT));
        searchForm.setFacets(Collections.singleton("category:" + categoryId));

        Category category = categoryClient.getCategory(categoryId);
        SearchResultWrapper products = searchClient.performSearch(searchForm);

        return new APICategory(category, products);
    }

}