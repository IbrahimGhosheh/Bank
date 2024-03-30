package com.assignment.accountservice.service.impl;

import com.assignment.accountservice.dto.AccountDTO;
import com.assignment.accountservice.model.Account;
import com.assignment.accountservice.service.AccountMapper;
import org.springframework.stereotype.Service;

@Service
public class AccountMapperImpl implements AccountMapper {
    @Override
    public AccountDTO fromAccount(Account account) {
        return new AccountDTO(
                account.getCustomerId(),
                account.getBalance(),
                account.getCurrency(),
                account.getStatus(),
                account.getType()
        );
    }

    @Override
    public Account fromAccountDTO(AccountDTO accountDTO) {
        return Account.builder()
                .customerId(accountDTO.customerId())
                .balance(accountDTO.balance())
                .currency(accountDTO.currency())
                .status(accountDTO.status())
                .type(accountDTO.type())
                .build();
    }
}
