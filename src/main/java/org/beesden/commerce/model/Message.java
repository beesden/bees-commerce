package org.beesden.commerce.model;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	private String key;
	private Object[] parameters;

	public String getKey() {
		return key;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

}