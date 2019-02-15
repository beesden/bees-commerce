package org.beesden.commerce.common.model.search;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SearchResult {

	private String id;
	private String title;
	private Map<String, List<String>> metadata;

}

