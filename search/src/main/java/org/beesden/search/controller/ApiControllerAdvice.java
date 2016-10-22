package org.beesden.search.controller;

import lombok.extern.slf4j.Slf4j;
import org.beesden.common.exception.NotFoundException;
import org.beesden.common.model.ResponseMessage;
import org.springframework.data.mapping.PropertyReferenceException;
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
	Set<ResponseMessage> handleArgumentException( MethodArgumentNotValidException exception ) {
		return exception.getBindingResult()
				.getFieldErrors()
				.stream()
				.map( error -> new ResponseMessage( error.getField() + "." + error.getCode(), error.getArguments() ) )
				.collect( Collectors.toSet() );
	}

	@ExceptionHandler
	@ResponseBody
	@ResponseStatus( HttpStatus.NOT_FOUND )
	ResponseMessage handleException( NotFoundException exception ) {
		return new ResponseMessage( "not.found", exception.getEntity() );
	}

	@ExceptionHandler
	@ResponseBody
	@ResponseStatus( HttpStatus.INTERNAL_SERVER_ERROR )
	ResponseMessage handleException( PropertyReferenceException exception ) {
		return new ResponseMessage( "invalid.reference", exception.getPropertyName() );
	}

	@ExceptionHandler
	@ResponseBody
	@ResponseStatus( HttpStatus.INTERNAL_SERVER_ERROR )
	void handleException( Exception exception ) {
		log.error( exception.getMessage() );
		exception.printStackTrace();
	}

}
