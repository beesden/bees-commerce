package org.beesden.commerce.common.model.search;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.beesden.commerce.common.model.EntityType;
import org.beesden.commerce.common.model.PagedRequest;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchForm extends PagedRequest {

	private String term;
	private Set<String> ids;
	private Set<EntityType> types;
	private Set<String> facets = new HashSet<>();

}

