package org.beesden.commerce.common.client;

import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.PagedResponse;
import org.beesden.commerce.common.model.commerce.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@FeignClient("http://product-client")
public interface ProductClient {

    @RequestMapping(method = RequestMethod.POST)
    String createProduct(Product product);

    @RequestMapping(value = "/{productKey}", method = RequestMethod.DELETE)
    void deleteProduct(@PathVariable("productKey") String productKey);

    @RequestMapping(value = "/{productKey}", method = RequestMethod.GET)
    Product getProduct(@PathVariable("productKey") String productKey);

    @RequestMapping(method = RequestMethod.GET)
    PagedResponse<Product> listProducts(@Valid PagedRequest pagination);

    @RequestMapping(value = "/{productKey}", method = RequestMethod.PUT)
    void updateProduct(@PathVariable("productKey") String productKey, Product product);

}