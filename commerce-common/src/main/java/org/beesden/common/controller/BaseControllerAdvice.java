package org.beesden.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.beesden.common.exception.NotFoundException;
import org.beesden.common.model.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Handle all errors within the application in one class. If something goes wrong during execution,
 * we should be returning error codes rather than server error pages.
 *
 * @author Pulse Innovations Ltd
 */
@ControllerAdvice
@Slf4j
public class BaseControllerAdvice {

	@ExceptionHandler
	@ResponseBody
	@ResponseStatus( HttpStatus.INTERNAL_SERVER_ERROR )
	void handleException( Exception exception ) {
		log.error( exception.getMessage() );
		exception.printStackTrace();
	}

	@ExceptionHandler
	@ResponseBody
	@ResponseStatus( HttpStatus.NOT_FOUND )
	ResponseMessage handleException( NotFoundException exception ) {
		return new ResponseMessage( "not.found", exception.getEntity() );
	}

}
