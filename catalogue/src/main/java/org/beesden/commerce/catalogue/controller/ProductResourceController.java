package org.beesden.commerce.catalogue.controller;

import org.beesden.commerce.catalogue.dao.ProductRepository;
import org.beesden.commerce.catalogue.domain.Product;
import org.beesden.commerce.common.client.ProductClient;
import org.beesden.commerce.common.exception.NotFoundException;
import org.beesden.commerce.common.exception.UniqueEntityException;
import org.beesden.commerce.common.model.EntityType;
import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.PagedResponse;
import org.beesden.commerce.common.model.commerce.ProductResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/product")
public class ProductResourceController implements ProductClient {

    private final ProductRepository productRepository;

    @Autowired
    ProductResourceController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void createProduct(@Valid @RequestBody ProductResource productResource) {

        Product product = productRepository.findOneByProductKey(productResource.getId());
        if (product == null) {
            product = new Product();
            product.setCreated(LocalDateTime.now());
            product.setCreatedBy("testuser");
        } else {
            throw new UniqueEntityException(EntityType.PRODUCT, product.getProductKey());
        }

        product.update(productResource);
        productRepository.save(product);

    }

    public void deleteProduct(@PathVariable String productKey) {

        productRepository.deleteByProductKey(productKey);

    }

    public ProductResource getProduct(@PathVariable String productKey) {

        Product product = productRepository.findOneByProductKey(productKey);
        if (product == null) {
            throw new NotFoundException(EntityType.PRODUCT, productKey);
        }

        return product.toResource();

    }

    public PagedResponse<ProductResource> listProducts(@Valid PagedRequest pagination) {

        Page<Product> products = productRepository.findAll(pagination.toPageable());
        List<ProductResource> productResourceList = products.getContent()
                .stream()
                .map(Product::toResource)
                .collect(Collectors.toList());

        return new PagedResponse<>(productResourceList, products.getTotalElements());

    }

    public void updateProduct(@PathVariable String productKey, @Valid @RequestBody ProductResource productResource) {

        Product product = productRepository.findOneByProductKey(productKey);
        if (product == null) {
            throw new NotFoundException(EntityType.PRODUCT, productKey);
        }

        product.update(productResource);
        productRepository.save(product);

    }

}