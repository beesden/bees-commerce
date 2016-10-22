package org.beesden.common.model;

import lombok.Data;
import org.beesden.common.EntityReference;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class SearchDocument {

	@NotNull
	EntityReference entity;
	@NotEmpty
	private String title;
	private Map<String, Set<String>> facets = new HashMap<>();

	public void addFacet( String name, String value ) {
		Set<String> values = facets.get( name );
		if ( values == null ) {
			values = new HashSet<>();
			facets.put( name, values );
		}
		values.add( value );
	}

}