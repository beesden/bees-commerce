package org.beesden.search.service;

import org.beesden.common.model.SearchDocument;
import org.beesden.search.model.SearchForm;
import org.beesden.search.model.SearchResult;

public interface SearchService {

	SearchResult performSearch( SearchForm searchForm );

	void removeFromIndex( SearchDocument searchDocument );

	void submitToIndex( SearchDocument searchDocument );

}