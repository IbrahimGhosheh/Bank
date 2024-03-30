package com.assignment.customerservice.exception;

public class DuplicatedEntityException extends ServiceException {
    public DuplicatedEntityException(String message) {
        super(message);
    }
}
