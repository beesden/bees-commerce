package org.beesden.commerce.model.api;

import lombok.Data;
import org.beesden.commerce.model.dto.ProductDTO;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Set;

@Data
public class Product {

	@NotEmpty
	private String id;
	@NotEmpty
	private String title;
	private String summary;
	private String description;
	private Set<Long> categories;
	private Set<Long> variants;

	public Product( ProductDTO product ) {
		id = product.getProductKey();
		title = product.getTitle();
		summary = product.getSummary();
		description = product.getDescription();
	}
}