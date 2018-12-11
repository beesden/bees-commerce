package org.beesden.commerce.api.controller;

import org.beesden.commerce.common.client.ProductClient;
import org.beesden.commerce.common.client.SearchClient;
import org.beesden.commerce.common.model.EntityType;
import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.commerce.Product;
import org.beesden.commerce.common.model.search.SearchForm;
import org.beesden.commerce.common.model.search.SearchResultWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping(path = "/product")
public class ProductController {

    @Autowired
    SearchClient searchClient;

    @Autowired
    ProductClient productClient;

    @RequestMapping(path = "/{productKey}", method = RequestMethod.GET)
    public Product getProduct(@PathVariable String productKey) {

        Product product = productClient.getProduct(productKey);

        return productClient.getProduct(productKey);

    }

    @RequestMapping(method = RequestMethod.GET)
    public SearchResultWrapper searchProducts(@Valid PagedRequest request) {

        Product product = new Product();
        product.setId("Test");
        product.setTitle("Toby test");
        searchClient.submitToIndex(product.toSearchDocument());

        SearchForm form =  new SearchForm();
        form.setTypes(Collections.singleton(EntityType.PRODUCT));
        return searchClient.performSearch(form);

    }

}