package org.beesden.common.model;

import lombok.Data;
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
}