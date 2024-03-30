package com.assignment.accountservice.service;

import com.assignment.accountservice.dto.AccountDTO;
import com.assignment.accountservice.model.Account;

import java.util.List;

public interface AccountService {

    Account createAccount(AccountDTO accountDTO);

    Account getAccount(String accountId);

    List<Account> getCustomerAccounts(String customerId);

    Account updateAccount(String accountId, AccountDTO accountDTO);

    void deleteAccount(String accountId);

    void deleteCustomerAccounts(String customerId);
}
