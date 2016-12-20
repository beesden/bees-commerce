package org.beesden.commerce.common.model;

import org.beesden.commerce.common.model.search.SearchDocument;

public interface Searchable {

	/**
	 * Convert an entity into a searchable document.
	 *
	 * @return search document
	 */
	SearchDocument toSearchDocument();

}
