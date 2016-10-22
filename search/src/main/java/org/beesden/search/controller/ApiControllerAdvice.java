package org.beesden.search.controller;

import lombok.extern.slf4j.Slf4j;
import org.beesden.common.controller.BaseControllerAdvice;
import org.beesden.common.model.ResponseMessage;
import org.beesden.search.exception.SearchAddIndexException;
import org.beesden.search.exception.SearchDeleteIndexException;
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
	@ResponseStatus( HttpStatus.NOT_ACCEPTABLE )
	ResponseMessage handleSearchAddIndexException( SearchAddIndexException exception ) {
		return new ResponseMessage( "search.error", exception.getEntity() );
	}

	@ExceptionHandler
	@ResponseBody
	@ResponseStatus( HttpStatus.BAD_REQUEST )
	ResponseMessage handleSearchDeleteIndexException( SearchDeleteIndexException exception ) {
		return new ResponseMessage( "search.error" );
	}

	@ExceptionHandler
	@ResponseBody
	@ResponseStatus( HttpStatus.NOT_FOUND )
	ResponseMessage handleSearchException( SearchException exception ) {
		return new ResponseMessage( "search.error" );
	}

}
