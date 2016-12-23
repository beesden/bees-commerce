package org.beesden.commerce.content.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class Category {

	@NotEmpty
	private String id;
	@NotEmpty
	private String title;
	private String summary;
	private String description;

}