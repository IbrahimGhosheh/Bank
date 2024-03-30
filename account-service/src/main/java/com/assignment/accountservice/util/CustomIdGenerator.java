package com.assignment.accountservice.util;

import com.assignment.accountservice.model.Account;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Random;

public class CustomIdGenerator implements IdentifierGenerator {

    private static final int MAX_ID = 999;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        String customerId = ((Account) object).getCustomerId();
        return generateAccountId(customerId);
    }

    private String generateAccountId(String customerId) {
        Random random = new Random();
        int randomDigits = random.nextInt(MAX_ID + 1);
        return customerId + String.format("%03d", randomDigits);
    }
}

