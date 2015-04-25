package org.beesden.commerce.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.validation.FieldError;

public class RestResponseWrapper implements Serializable {

	private static final long serialVersionUID = 1L;

	public Map<String, Object> content = new HashMap<>();
	private List<Message> messages = new ArrayList<>();

	public void addMessage(String key, Object[] arguments) {
		Message message = new Message();
		message.setKey(key);
		message.setParameters(arguments);
		messages.add(message);
	}

	public void addValidation(FieldError error) {
		String message = "forms." + error.getField() + ".validation." + error.getCode();
		addMessage(message.toLowerCase(), error.getArguments());
	}

	public void addContent(String key, Object value) {
		this.content.put(key, value);
	}

	public Map<String, Object> getContent() {
		return content;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setContent(Map<String, Object> content) {
		this.content = content;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
}