package com.assignment.accountservice.listener;

import com.assignment.accountservice.listener.event.DeleteCustomerEvent;
import com.assignment.accountservice.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventListener {

    private final AccountService accountService;

    public KafkaEventListener(AccountService accountService) {
        this.accountService = accountService;
    }

    @KafkaListener(
            topics = "customer",
            containerFactory = "customerKafkaListenerContainerFactory",
            groupId = "account-service"
    )
    public void customerListener(DeleteCustomerEvent event) {
        logger.info("Received customer event: " + event.toString());
        accountService.deleteCustomerAccounts(event.getCustomerId());
    }

    private final Logger logger = LoggerFactory.getLogger(KafkaEventListener.class);

}
