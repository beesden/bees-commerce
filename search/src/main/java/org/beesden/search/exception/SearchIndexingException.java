package org.beesden.search.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.beesden.common.EntityType;
import org.beesden.common.model.SearchDocument;

public class SearchIndexingException extends RuntimeException {

	@Getter
	private Entity entity;

	public SearchIndexingException( SearchDocument document ) {
		super( "Error indexing an entity" );
		entity = new Entity( document.getEntityType(), document.getId() );
	}

	@Data
	@AllArgsConstructor
	private class Entity {
		private EntityType type;
		private String entityId;
	}

}