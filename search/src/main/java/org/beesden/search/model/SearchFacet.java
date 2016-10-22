package org.beesden.search.model;

import lombok.Data;

import java.util.Map;

@Data
public class SearchFacet {

	private String name;
	private Map<String, Long> fields;

}