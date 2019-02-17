package org.beesden.commerce.common.client;

import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.PagedResponse;
import org.beesden.commerce.common.model.resource.ProductResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@FeignClient("catalogue-client/product")
public interface ProductClient {

    @RequestMapping(method = RequestMethod.POST)
    void createProduct(ProductResource productResource);

    @RequestMapping(value = "/{productKey}", method = RequestMethod.DELETE)
    void deleteProduct(@PathVariable("productKey") String productKey);

    @RequestMapping(value = "/{productKey}", method = RequestMethod.GET)
    ProductResource getProduct(@PathVariable("productKey") String productKey);

    @RequestMapping(method = RequestMethod.GET)
    PagedResponse<ProductResource> listProducts(@Valid PagedRequest pagination);

    @RequestMapping(value = "/{productKey}", method = RequestMethod.PUT)
    void updateProduct(@PathVariable("productKey") String productKey, ProductResource productResource);

}