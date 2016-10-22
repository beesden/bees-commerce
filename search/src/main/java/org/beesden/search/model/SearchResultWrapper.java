package org.beesden.search.model;

import lombok.Data;
import org.apache.lucene.facet.FacetResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SearchResultWrapper {

	private List<SearchFacet> facets = new ArrayList<>();
	private List<SearchResult> results = new ArrayList<>();
	private int total;

	public void addFacet( FacetResult facetResult ) {
		if ( facetResult != null ) {
			SearchFacet facet = new SearchFacet();
			facet.setName( facetResult.dim );
			facet.setFields( Arrays.stream( facetResult.labelValues )
					.collect( Collectors.toMap( fa -> fa.label, fa -> fa.value.longValue() ) ) );
			this.facets.add( facet );
		}
	}

	public void addResult( SearchResult result ) {
		this.results.add( result );
	}

}