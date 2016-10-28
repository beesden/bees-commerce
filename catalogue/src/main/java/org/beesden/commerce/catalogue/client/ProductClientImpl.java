package org.beesden.commerce.catalogue.client;

import org.beesden.commerce.catalogue.dao.ProductRepository;
import org.beesden.commerce.catalogue.domain.ProductDTO;
import org.beesden.common.client.ProductClient;
import org.beesden.common.exception.NotFoundException;
import org.beesden.common.model.EntityType;
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
public class ProductClientImpl implements ProductClient {

	private ProductRepository productRepository;

	@Autowired
	public ProductClientImpl( ProductRepository productRepository ) {
		this.productRepository = productRepository;
	}

	@Override
	public void createProduct( Product product ) {

		ProductDTO createdProduct = new ProductDTO();
		createdProduct.update( product );
		createdProduct.updateTimestamps();
		productRepository.save( createdProduct );
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
	public void updateProduct( String productKey, Product product ) {

		ProductDTO updatedProduct = productRepository.findOneByProductKey( productKey );
		if ( updatedProduct == null ) {
			throw new NotFoundException( EntityType.PRODUCT, productKey );
		}

		updatedProduct.update( product );
		updatedProduct.updateTimestamps();
		productRepository.save( updatedProduct );
	}

}