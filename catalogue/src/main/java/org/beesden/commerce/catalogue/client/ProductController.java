package org.beesden.commerce.catalogue.client;

import org.beesden.commerce.catalogue.dao.ProductRepository;
import org.beesden.commerce.catalogue.domain.ProductDTO;
import org.beesden.commerce.common.client.SearchClient;
import org.beesden.commerce.common.exception.NotFoundException;
import org.beesden.commerce.common.model.EntityType;
import org.beesden.commerce.common.model.PagedRequest;
import org.beesden.commerce.common.model.PagedResponse;
import org.beesden.commerce.common.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductController {

	private ProductRepository productRepository;
	private SearchClient searchClient;

	@Autowired
	public ProductController(ProductRepository productRepository, SearchClient searchClient) {
		this.productRepository = productRepository;
		this.searchClient = searchClient;
	}

	@RequestMapping(method = RequestMethod.POST)
	public void createProduct(@Valid @RequestBody Product product) {
		ProductDTO createdProduct = new ProductDTO();
		createdProduct.update(product);
		createdProduct.updateTimestamps();
		createdProduct = productRepository.save(createdProduct);
		searchClient.submitToIndex(createdProduct.toSearchDocument());
	}

	@RequestMapping(value = "/{productId}", method = RequestMethod.DELETE)
	public void deleteProduct(@PathVariable String productKey) {
		productRepository.deleteByProductKey(productKey);
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public Product getProduct(@PathVariable String productKey) {

		ProductDTO product = productRepository.findOneByProductKey(productKey);
		if (product == null) {
			throw new NotFoundException(EntityType.PRODUCT, productKey);
		}

		return product.toProduct();
	}

	@RequestMapping(value = "/{productId}", method = RequestMethod.GET)
	public PagedResponse<Product> listProducts(@Valid PagedRequest pagination) {

		Page<ProductDTO> pagedProducts = productRepository.findAll(pagination.toPageable());
		List<Product> productList = pagedProducts.getContent().stream().map(ProductDTO::toProduct).collect(Collectors.toList());

		return new PagedResponse<>(productList, pagedProducts.getTotalElements());
	}

	@RequestMapping(method = RequestMethod.GET)
	public void indexProducts() {
		List<ProductDTO> products = productRepository.findAll();

		int i = 0;
		for (ProductDTO product : products) {
			searchClient.submitToIndex(product.toSearchDocument());
			System.out.println("Indexed: " + ++i + " of " + products.size());
		}
	}

	@RequestMapping(value = "/{productId}", method = RequestMethod.PUT)
	public void updateProduct(@PathVariable String productKey, @Valid @RequestBody Product product) {

		ProductDTO updatedProduct = productRepository.findOneByProductKey(productKey);
		if (updatedProduct == null) {
			throw new NotFoundException(EntityType.PRODUCT, productKey);
		}

		updatedProduct.update(product);
		updatedProduct.updateTimestamps();
		updatedProduct = productRepository.save(updatedProduct);
		searchClient.submitToIndex(updatedProduct.toSearchDocument());
	}

}