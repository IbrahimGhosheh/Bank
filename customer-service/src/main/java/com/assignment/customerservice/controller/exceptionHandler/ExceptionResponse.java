package com.assignment.customerservice.controller.exceptionHandler;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {
    private String errorMessage;
    private HttpStatus httpStatus;
}

