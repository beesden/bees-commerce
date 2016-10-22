package org.beesden.common.model;

import lombok.Data;
import org.beesden.common.EntityReference;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Data
public class SearchDocument {

	@NotNull
	EntityReference entity;
	@NotEmpty
	private String title;
	private Map<String, String> facets = new HashMap<>();

}