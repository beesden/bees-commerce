package org.beesden.commerce.common.model.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.beesden.commerce.common.model.PagedResponse;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchResource extends PagedResponse<SearchResource.SearchResult> {

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


	@Data
	public static class SearchResult {
		private String id;
		private String title;
		private Map<String, List<String>> metadata;
	}

}

