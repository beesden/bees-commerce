package org.beesden.search.service;

import org.beesden.common.model.SearchDocument;
import org.beesden.search.model.SearchForm;
import org.beesden.search.model.SearchResultWrapper;

public interface SearchService {

	void clearIndex();

	SearchResultWrapper performSearch( SearchForm searchForm );

	void removeFromIndex( SearchDocument searchDocument );

	void submitToIndex( SearchDocument searchDocument );

}