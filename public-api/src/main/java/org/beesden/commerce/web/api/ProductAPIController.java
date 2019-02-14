package org.beesden.commerce.web.api;

import org.beesden.commerce.common.client.ProductClient;
import org.beesden.commerce.common.client.SearchClient;
import org.beesden.commerce.common.model.commerce.ProductResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/product")
public class ProductAPIController {

    private final ProductClient productClient;

    @Autowired
    public ProductAPIController(ProductClient productClient) {
        this.productClient = productClient;
    }

    @RequestMapping(path = "/product/{productKey}", method = RequestMethod.GET)
    public ProductResource getProduct(@PathVariable String productKey) {
        return productClient.getProduct(productKey);
    }

}