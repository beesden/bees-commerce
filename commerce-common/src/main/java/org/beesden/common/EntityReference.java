package org.beesden.common;

import lombok.Data;

@Data
public class EntityReference {

	private long entityId;
	private EntityType type;

}