package com.assignment.accountservice.exception;

public class MaxAccountsReachedException extends ServiceException{
    public MaxAccountsReachedException(String message) {
        super(message);
    }
}
