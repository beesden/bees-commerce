package org.beesden.commerce.web.api;

import org.beesden.commerce.common.client.CategoryClient;
import org.beesden.commerce.common.client.ProductClient;
import org.beesden.commerce.common.client.SearchClient;
import org.beesden.commerce.common.model.commerce.Category;
import org.beesden.commerce.common.model.commerce.Product;
import org.beesden.commerce.common.model.search.SearchDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;

@RestController
@RequestMapping(path = "/bootstrap")
public class BootstrapController {

    @Autowired
    SearchClient searchClient;

    @Autowired
    CategoryClient categoryClient;

    @Autowired
    ProductClient productClient;

    @RequestMapping(method = RequestMethod.GET)
    public long populate() {

        int total = 0;
        for (int i = 0; i < 10; i++) {

            long random = Math.round(Math.random() * 10) + 5;

            // Add category
            Category category = new Category();
            category.setId("cat_" + i);
            category.setTitle("Category " + i);
            categoryClient.createCategory(category);

            // Add products
            for (int ii = 0; ii < random; ii++) {

                Product product = new Product();
                product.setId("p" + ++total);
                product.setTitle("Product #" + total);
                product.setDescription("Sample product #" + total);
                productClient.createProduct(product);

                SearchDocument document = product.toSearchDocument();
                document.setFacets(new HashMap<>());
                document.getFacets().put("category", Collections.singleton(category.getId()));
                searchClient.submitToIndex(document);

            }

        }

        return total;

    }

}