package org.beesden.search.exception;

/**
 * An exception which is thrown when an error occurs when removing an entity from the index.
 */
public class SearchDeleteIndexException extends RuntimeException {

	public SearchDeleteIndexException( String message ) {
		super( message );
	}
}