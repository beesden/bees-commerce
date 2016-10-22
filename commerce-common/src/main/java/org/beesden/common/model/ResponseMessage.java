package org.beesden.common.model;

import lombok.Data;

@Data
public class ResponseMessage {
	private String key;
	private Object params;

	public ResponseMessage( String key, Object params ) {
		this.key = "error." + key;
		this.params = params;
	}
}
