package org.beesden.commerce.common.model.search;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.beesden.commerce.common.model.PagedResponse;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchResultWrapper extends PagedResponse<SearchResult> {

	private Map<String, Map<String, Integer>> facets;

}

