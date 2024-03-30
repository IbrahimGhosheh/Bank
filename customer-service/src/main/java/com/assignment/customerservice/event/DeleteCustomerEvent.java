package com.assignment.customerservice.event;


public record DeleteCustomerEvent(String customerId) implements CustomerEvent {

}
