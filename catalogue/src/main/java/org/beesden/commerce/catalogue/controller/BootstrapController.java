package org.beesden.commerce.catalogue.controller;

import lombok.extern.slf4j.Slf4j;
import org.beesden.commerce.catalogue.dao.ProductRepository;
import org.beesden.commerce.catalogue.domain.Category;
import org.beesden.commerce.catalogue.domain.Product;
import org.beesden.commerce.common.client.SearchClient;
import org.beesden.commerce.common.model.commerce.ProductResource;
import org.beesden.commerce.common.model.search.SearchDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public Boolean reindex() {

        List<Product> products = productRepository.findAll();
        searchClient.clearIndex();

        products.forEach(product -> {

            ProductResource productResource = product.toProduct();
            Set<String> categories = product.getCategories().stream().map(Category::getCategoryId).collect(Collectors.toSet());
            log.info("Product " + products.indexOf(product) + " of " + products.size() + ": " + product.getTitle());

            SearchDocument document = productResource.toSearchDocument();
            document.setFacets(new HashMap<>());
            document.getFacets().put("category", categories);
            searchClient.submitToIndex(document);

        });

        return true;

    }

}