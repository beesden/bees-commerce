package org.beesden.search.controller;

import lombok.extern.slf4j.Slf4j;
import org.beesden.common.controller.BaseControllerAdvice;
import org.beesden.common.model.ResponseMessage;
import org.beesden.search.exception.SearchEntityException;
import org.beesden.search.exception.SearchException;
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
public class ApiControllerAdvice extends BaseControllerAdvice {

	@ExceptionHandler
	@ResponseBody
	@ResponseStatus( HttpStatus.BAD_REQUEST )
	ResponseMessage handleSearchEntityException( SearchEntityException exception ) {
		return new ResponseMessage( "error.search.entity", exception.getEntity() );
	}

	@ExceptionHandler
	@ResponseBody
	@ResponseStatus( HttpStatus.BAD_REQUEST )
	ResponseMessage handleSearchException( SearchException exception ) {
		return new ResponseMessage( "error.search" );
	}

}
