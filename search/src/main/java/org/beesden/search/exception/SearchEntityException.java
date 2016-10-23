package org.beesden.search.exception;

import lombok.Getter;
import org.beesden.common.EntityReference;

/**
 * An exception which is thrown when an error occurs when adding an entity to the index.
 */
public class SearchEntityException extends RuntimeException {

	@Getter
	private EntityReference entity;

	public SearchEntityException( String message, EntityReference entity, Throwable e ) {
		super( message, e );
		this.entity = entity;
	}

}