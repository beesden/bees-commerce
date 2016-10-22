package org.beesden.search.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SearchResult {

	private List<SearchResultEntity> results = new ArrayList<>();

	public void addEntity( SearchResultEntity entity ) {
		results.add( entity );
	}
}