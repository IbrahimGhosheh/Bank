package com.assignment.accountservice.controller.exceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {
    private String errorMessage;
    private HttpStatus httpStatus;
}

