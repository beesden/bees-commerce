package org.beesden.commerce.service.impl;

import org.beesden.commerce.EntityType;
import org.beesden.commerce.dao.ProductRepository;
import org.beesden.commerce.exception.NotFoundException;
import org.beesden.commerce.model.api.PagedRequest;
import org.beesden.commerce.model.api.PagedResponse;
import org.beesden.commerce.model.api.Product;
import org.beesden.commerce.model.dto.ProductDTO;
import org.beesden.commerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

	private ProductRepository productRepository;

	@Autowired
	public ProductServiceImpl( ProductRepository productRepository ) {
		this.productRepository = productRepository;
	}

	@Transactional
	public Product getProduct( String productKey ) {

		ProductDTO product = productRepository.getOneByProductKey( productKey );
		if ( product == null ) {
			throw new NotFoundException( EntityType.PRODUCT, productKey );
		}

		return new Product( product );

	}

	@Override
	public void deleteProduct( String productKey ) {
		productRepository.deleteByProductKey( productKey );
	}

	@Override
	public Product createProduct( Product product ) {

		ProductDTO createdProduct = new ProductDTO();
		createdProduct.update( product );
		createdProduct.updateTimestamps();
		createdProduct = productRepository.save( createdProduct );

		return new Product( createdProduct );
	}

	@Override
	public Product updateProduct( String productKey, Product product ) {

		ProductDTO updatedProduct = productRepository.getOneByProductKey( productKey );
		if ( updatedProduct == null ) {
			throw new NotFoundException( EntityType.PRODUCT, productKey );
		}

		updatedProduct.update( product );
		updatedProduct.updateTimestamps();
		updatedProduct = productRepository.save( updatedProduct );

		return new Product( updatedProduct );
	}

	@Transactional
	public PagedResponse<Product> listProducts( PagedRequest pagination ) {

		Page<ProductDTO> pagedProducts = productRepository.findAll( pagination.toPageable() );
		List<Product> productList = pagedProducts.getContent()
				.stream()
				.map( Product::new )
				.collect( Collectors.toList() );

		return new PagedResponse<>( productList, pagedProducts.getTotalElements() );

	}

}