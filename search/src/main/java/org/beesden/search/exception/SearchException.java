package org.beesden.search.exception;

/**
 * An exception which is thrown when an error occurs whilst a search query is being performed.
 */
public class SearchException extends RuntimeException {

	public SearchException( String message, Throwable e ) {
		super( message, e );
	}
}