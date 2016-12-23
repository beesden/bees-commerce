package org.beesden.commerce.content.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Map;

@Data
public class Content {

	private long id;
	@NotEmpty
	private String title;
	private String description;
	private Map<Long, String> data;

}