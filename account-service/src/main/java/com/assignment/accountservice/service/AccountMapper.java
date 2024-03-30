package com.assignment.accountservice.service;

import com.assignment.accountservice.dto.AccountDTO;
import com.assignment.accountservice.model.Account;

public interface AccountMapper {
    AccountDTO fromAccount(Account account);

    Account fromAccountDTO(AccountDTO accountDTO);
}
