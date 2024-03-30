package com.assignment.accountservice.listener.event;

public record DeleteCustomerEvent(String customerId) implements CustomerEvent {

}
