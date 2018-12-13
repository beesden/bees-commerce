package org.beesden.commerce.web.controller;

import org.beesden.commerce.common.client.ProductClient;
import org.beesden.commerce.common.client.SearchClient;
import org.beesden.commerce.common.model.commerce.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/product")
public class ProductController {

    @Autowired
    SearchClient searchClient;

    @Autowired
    ProductClient productClient;

    @RequestMapping(path = "/product/{productKey}", method = RequestMethod.GET)
    public Product viewProduct(@PathVariable String productKey) {
        return productClient.getProduct(productKey);
    }

}