package org.beesden.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.beesden.commerce.common.EntityType;

public class NotFoundException extends RuntimeException {

	@Getter
	private Entity entity;

	public NotFoundException( EntityType type, String entityId ) {
		super( "Unable to find dto" );
		entity = new Entity( type, entityId );
	}

	@Data
	@AllArgsConstructor
	private class Entity {
		private EntityType type;
		private String entityId;
	}

}