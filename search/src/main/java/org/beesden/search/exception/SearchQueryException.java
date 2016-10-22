package org.beesden.search.exception;

/**
 * An exception which is thrown when an error occurs whilst a search query is being built.
 */
public class SearchQueryException extends RuntimeException {

	public SearchQueryException( String message ) {
		super( message );
	}
}