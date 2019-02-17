package org.beesden.commerce.catalogue.controller;

import lombok.extern.slf4j.Slf4j;
import org.beesden.commerce.catalogue.dao.ProductRepository;
import org.beesden.commerce.catalogue.domain.Product;
import org.beesden.commerce.common.client.SearchClient;
import org.beesden.commerce.common.model.resource.ProductResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/bootstrap")
public class BootstrapController {

    private final ProductRepository productRepository;
    private final SearchClient searchClient;

    @Autowired
    public BootstrapController(ProductRepository productRepository, SearchClient searchClient) {
        this.productRepository = productRepository;
        this.searchClient = searchClient;
    }

    /**
     * Use this endpoint to repopulate the index
     */
    @GetMapping
    public void reindex() {

        List<Product> products = productRepository.findAll();
        searchClient.clearIndex();

        products.forEach(product -> {

            ProductResource resource = product.toResource();
            log.info("Product " + products.indexOf(product) + " of " + products.size() + ": " + product.getTitle());
            searchClient.submitToIndex(resource.toSearchDocument());

        });

    }

}