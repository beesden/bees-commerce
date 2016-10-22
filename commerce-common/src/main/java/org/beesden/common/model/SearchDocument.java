package org.beesden.common.model;

import lombok.Data;
import org.beesden.common.EntityType;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class SearchDocument {

	@NotEmpty
	EntityType entityType;
	@NotEmpty
	private String id;
	@NotEmpty
	private String title;
	private String summary;
	private String description;


}