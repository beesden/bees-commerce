package org.beesden.commerce.common.model;

import lombok.Data;

@Data
public class ResponseMessage {
	private String key;
	private Object params;

	public ResponseMessage( String key ) {
		this.key = "error." + key;
	}

	public ResponseMessage( String key, Object params ) {
		this( key );
		this.params = params;
	}
}
