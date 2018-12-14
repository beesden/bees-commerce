package org.beesden.commerce.catalogue;

import org.beesden.commerce.common.exception.NotFoundException;
import org.beesden.commerce.common.exception.UniqueEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class ResponseExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity handleNotFoundException() {
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UniqueEntityException.class)
    public final ResponseEntity handleUniqueEntityException() {
        return new ResponseEntity(HttpStatus.CONFLICT);
    }

}
