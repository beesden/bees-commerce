package org.beesden.commerce.common.model.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.beesden.commerce.common.model.PagedResponse;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchResultWrapper extends PagedResponse<SearchResult> {

	private List<SearchResultFacets> facets;

	@Data
	public static class SearchResultFacets {
		private String name;
		private List<SearchResultFacet> facets;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SearchResultFacet {
		private String name;
		private int count;
	}

}

