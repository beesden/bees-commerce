package org.beesden.commerce.common.exception;

import lombok.Getter;
import org.beesden.commerce.common.model.EntityReference;
import org.beesden.commerce.common.model.EntityType;

public class NotFoundException extends RuntimeException {

	@Getter
	private EntityReference entity;

	public NotFoundException( EntityType type, String entityId ) {
		super( "Unable to find dto" );
		entity = new EntityReference( type, entityId );
	}

}