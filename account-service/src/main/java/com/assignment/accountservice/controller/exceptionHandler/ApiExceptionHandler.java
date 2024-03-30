package com.assignment.accountservice.controller.exceptionHandler;

import com.assignment.accountservice.exception.EntityNotFoundException;
import com.assignment.accountservice.exception.MaxAccountsReachedException;
import com.assignment.accountservice.exception.SalaryAccountExistsException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(
            {
            MethodArgumentNotValidException.class,
            MismatchedInputException.class,
            HttpMessageNotReadableException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> validationExceptions(Throwable throwable) {
        return prepareExceptionResponse(
                "EX.MISSING_PARAMETERS " + throwable.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxAccountsReachedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ExceptionResponse> maxAccountReachedException(MaxAccountsReachedException maxAccountsReachedException){
        return prepareExceptionResponse(
                maxAccountsReachedException.getMessage(),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(SalaryAccountExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ExceptionResponse> salaryAccountExistsException(SalaryAccountExistsException salaryAccountExistsException){
        return prepareExceptionResponse(
                salaryAccountExistsException.getMessage(),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionResponse> entityNotFoundException(EntityNotFoundException entityNotFoundException){
        return prepareExceptionResponse(
                entityNotFoundException.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> generalExceptionHandler(Throwable exception) {
        logger.error("Exception: " + exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private ResponseEntity<ExceptionResponse> prepareExceptionResponse(String message, HttpStatus status) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(message, status);
        return ResponseEntity.status(status).body(exceptionResponse);
    }

    private final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

}
