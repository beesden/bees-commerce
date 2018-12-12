package org.beesden.commerce.catalogue.controller;

import org.beesden.commerce.catalogue.dao.ProductRepository;
import org.beesden.commerce.catalogue.domain.ProductDTO;
import org.beesden.commerce.common.client.ProductClient;
import org.beesden.commerce.common.exception.NotFoundException;
import org.beesden.commerce.common.model.EntityType;
import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.PagedResponse;
import org.beesden.commerce.common.model.commerce.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/product")
public class ProductController implements ProductClient {

    private ProductRepository productRepository;

    @Autowired
    ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void createProduct(@Valid @RequestBody Product product) {

        ProductDTO target = productRepository.findOneByProductKey(product.getId());
        if (target == null) {
            target = new ProductDTO();
            target.setCreated(LocalDateTime.now());
            target.setCreatedBy("testuser");
        }

        target.update(product);
        productRepository.save(target);

    }

    public void deleteProduct(@PathVariable String productKey) {

        productRepository.deleteByProductKey(productKey);

    }

    public Product getProduct(@PathVariable String productKey) {

        ProductDTO product = productRepository.findOneByProductKey(productKey);
        // todo - abstract
        if (product == null) {
            throw new NotFoundException(EntityType.PRODUCT, productKey);
        }

        return product.toProduct();

    }

    public PagedResponse<Product> listProducts(@Valid PagedRequest pagination) {

        Page<ProductDTO> pagedProducts = productRepository.findAll(pagination.toPageable());
        List<Product> productList = pagedProducts.getContent()
                .stream()
                .map(ProductDTO::toProduct)
                .collect(Collectors.toList());

        return new PagedResponse<>(productList, pagedProducts.getTotalElements());

    }

    public void updateProduct(@PathVariable String productKey, @Valid @RequestBody Product product) {

        ProductDTO target = productRepository.findOneByProductKey(productKey);
        // todo - abstract
        if (target == null) {
            throw new NotFoundException(EntityType.PRODUCT, productKey);
        }

        target.update(product);
        productRepository.save(target);

    }

}