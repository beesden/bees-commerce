package org.beesden.commerce.catalogue.controller;

import org.beesden.commerce.catalogue.dao.CategoryRepository;
import org.beesden.commerce.catalogue.dao.ProductRepository;
import org.beesden.commerce.catalogue.domain.CategoryDTO;
import org.beesden.commerce.catalogue.domain.ProductDTO;
import org.beesden.commerce.common.client.SearchClient;
import org.beesden.commerce.common.model.commerce.Product;
import org.beesden.commerce.common.model.search.SearchDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

        List<ProductDTO> products = productRepository.findAll();
        searchClient.clearIndex();

        products.forEach(productDTO -> {

            Product product = productDTO.toProduct();
            Set<String> categories = productDTO.getCategories().stream().map(CategoryDTO::getCategoryId).collect(Collectors.toSet());

            SearchDocument document = product.toSearchDocument();
            document.setFacets(new HashMap<>());
            document.getFacets().put("category", categories);
            searchClient.submitToIndex(document);

        });

        return true;

    }

}