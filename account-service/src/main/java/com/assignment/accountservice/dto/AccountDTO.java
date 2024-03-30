package com.assignment.accountservice.dto;

import com.assignment.accountservice.enums.AccountStatus;
import com.assignment.accountservice.enums.AccountType;
import com.assignment.accountservice.enums.Currency;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AccountDTO(
        @NotNull
        String customerId,
        @NotNull
        BigDecimal balance,
        @NotNull
        Currency currency,
        @NotNull
        AccountStatus status,
        @NotNull
        AccountType type
) {
}
