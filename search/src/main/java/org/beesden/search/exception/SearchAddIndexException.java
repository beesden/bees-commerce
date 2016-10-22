package org.beesden.search.exception;

import lombok.Getter;
import org.beesden.common.EntityReference;
import org.beesden.common.model.SearchDocument;

/**
 * An exception which is thrown when an error occurs when adding an entity to the index.
 */
public class SearchAddIndexException extends RuntimeException {

	@Getter
	private EntityReference entity;

	public SearchAddIndexException( SearchDocument document ) {
		super( "Error indexing an entity" );
		entity = document.getEntity();
	}

}