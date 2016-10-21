package org.beesden.commerce.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.beesden.commerce.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handle all errors within the application in one class. If something goes wrong during execution,
 * we should be returning error codes rather than server error pages.
 *
 * @author Pulse Innovations Ltd
 */
@ControllerAdvice
@Slf4j
public class ApiControllerAdvice {

	@ExceptionHandler
	@ResponseBody
	@ResponseStatus( HttpStatus.BAD_REQUEST )
	Set<Message> handleArgumentException( MethodArgumentNotValidException exception ) {
		return exception.getBindingResult()
				.getFieldErrors()
				.stream()
				.map( error -> new Message( error.getField() + "." + error.getCode(), error.getArguments() ) )
				.collect( Collectors.toSet() );
	}

	@ExceptionHandler
	@ResponseBody
	@ResponseStatus( HttpStatus.NOT_FOUND )
	Message handleException( NotFoundException exception ) {
		return new Message( "not.found", exception.getEntity() );
	}

	@ExceptionHandler
	@ResponseBody
	@ResponseStatus( HttpStatus.INTERNAL_SERVER_ERROR )
	void handleException( Exception exception ) {
		log.error( exception.getMessage() );
	}

	@Data
	private static final class Message {
		private String key;
		private Object params;

		private Message( String key, Object params ) {
			this.key = "error." + key;
			this.params = params;
		}
	}

}
