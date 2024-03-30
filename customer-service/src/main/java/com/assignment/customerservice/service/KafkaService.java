package com.assignment.customerservice.service;

import com.assignment.customerservice.event.CustomerEvent;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

public interface KafkaService {
    CompletableFuture<SendResult<String, CustomerEvent>> sendCustomerEvent(CustomerEvent customerEvent);
}
