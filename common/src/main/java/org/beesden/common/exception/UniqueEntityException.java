package org.beesden.common.exception;

import lombok.Getter;
import org.beesden.common.model.EntityReference;
import org.beesden.common.model.EntityType;

public class UniqueEntityException extends RuntimeException {

	@Getter
	private EntityReference entity;

	public UniqueEntityException( EntityType type, String entityId ) {
		super( "Unable to find dto" );
		entity = new EntityReference( type, entityId );
	}

}