package org.beesden.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class EntityReference {

	@NotNull
	public EntityType type;
	@NotEmpty
	public String id;

}

