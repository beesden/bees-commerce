package org.beesden.search.service;

import org.beesden.common.EntityReference;
import org.beesden.common.model.SearchDocument;
import org.beesden.search.model.SearchForm;
import org.beesden.search.model.SearchResultWrapper;

public interface SearchService {

	void clearIndex();

	SearchResultWrapper performSearch( SearchForm searchForm );

	void removeFromIndex( EntityReference entity );

	void submitToIndex( SearchDocument searchDocument );

}