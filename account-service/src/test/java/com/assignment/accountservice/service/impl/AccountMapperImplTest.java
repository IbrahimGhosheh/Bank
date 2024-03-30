package com.assignment.accountservice.service.impl;

import com.assignment.accountservice.dto.AccountDTO;
import com.assignment.accountservice.enums.AccountStatus;
import com.assignment.accountservice.enums.AccountType;
import com.assignment.accountservice.enums.Currency;
import com.assignment.accountservice.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountMapperImplTest {

    private AccountMapperImpl accountMapper;

    @BeforeEach
    void setup() {
        accountMapper = new AccountMapperImpl();
    }

    @Test
    public void test_returns_account_dto_with_same_account_fields() {
        Account account = Account.builder()
                .customerId("1234567890")
                .balance(BigDecimal.valueOf(1000))
                .currency(Currency.USD)
                .status(AccountStatus.ACTIVATED)
                .type(AccountType.SAVING)
                .build();

        AccountDTO accountDTO = accountMapper.fromAccount(account);

        assertEquals(account.getCustomerId(), accountDTO.customerId());
        assertEquals(account.getBalance(), accountDTO.balance());
        assertEquals(account.getCurrency(), accountDTO.currency());
        assertEquals(account.getStatus(), accountDTO.status());
        assertEquals(account.getType(), accountDTO.type());
    }

    @Test
    public void test_return_instance_with_correctly_mapped_fields() {
        AccountDTO accountDTO = new AccountDTO("1234567890", BigDecimal.valueOf(1000), Currency.USD, AccountStatus.ACTIVATED, AccountType.SAVING);

        Account account = accountMapper.fromAccountDTO(accountDTO);

        assertEquals(accountDTO.customerId(), account.getCustomerId());
        assertEquals(accountDTO.balance(), account.getBalance());
        assertEquals(accountDTO.currency(), account.getCurrency());
        assertEquals(accountDTO.status(), account.getStatus());
        assertEquals(accountDTO.type(), account.getType());
    }
}