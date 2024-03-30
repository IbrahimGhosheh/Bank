package com.assignment.customerservice.service.impl;

import com.assignment.customerservice.event.CustomerEvent;
import com.assignment.customerservice.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaServiceImpl implements KafkaService {

    private final KafkaTemplate<String, CustomerEvent> kafkaTemplate;

    public KafkaServiceImpl(KafkaTemplate<String, CustomerEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public CompletableFuture<SendResult<String, CustomerEvent>> sendCustomerEvent(CustomerEvent customerEvent) {
        logger.info("Sending customer event: " + customerEvent.toString());
        return kafkaTemplate.send("customer", customerEvent);
    }

    private final Logger logger = LoggerFactory.getLogger(KafkaServiceImpl.class);
}
