package org.beesden.commerce.service.impl;

import org.beesden.commerce.common.EntityType;
import org.beesden.commerce.dao.ProductRepository;
import org.beesden.commerce.model.ProductDTO;
import org.beesden.commerce.service.ProductService;
import org.beesden.common.exception.NotFoundException;
import org.beesden.common.model.PagedRequest;
import org.beesden.common.model.PagedResponse;
import org.beesden.common.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	private ProductRepository productRepository;

	@Autowired
	public ProductServiceImpl( ProductRepository productRepository ) {
		this.productRepository = productRepository;
	}

	@Override
	public Product createProduct( Product product ) {

		ProductDTO createdProduct = new ProductDTO();
		createdProduct.update( product );
		createdProduct.updateTimestamps();
		createdProduct = productRepository.save( createdProduct );

		return createdProduct.toProduct();
	}

	@Override
	public void deleteProduct( String productKey ) {
		productRepository.deleteByProductKey( productKey );
	}

	@Override
	public Product getProduct( String productKey ) {

		ProductDTO product = productRepository.findOneByProductKey( productKey );
		if ( product == null ) {
			throw new NotFoundException( EntityType.PRODUCT, productKey );
		}

		return product.toProduct();

	}

	@Override
	public PagedResponse<Product> listProducts( PagedRequest pagination ) {

		Page<ProductDTO> pagedProducts = productRepository.findAll( pagination.toPageable() );
		List<Product> productList = pagedProducts.getContent()
				.stream()
				.map( ProductDTO::toProduct )
				.collect( Collectors.toList() );

		return new PagedResponse<>( productList, pagedProducts.getTotalElements() );

	}

	@Override
	public Product updateProduct( String productKey, Product product ) {

		ProductDTO updatedProduct = productRepository.findOneByProductKey( productKey );
		if ( updatedProduct == null ) {
			throw new NotFoundException( EntityType.PRODUCT, productKey );
		}

		updatedProduct.update( product );
		updatedProduct.updateTimestamps();
		updatedProduct = productRepository.save( updatedProduct );

		return updatedProduct.toProduct();
	}

}