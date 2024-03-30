package com.assignment.accountservice.service.impl;

import com.assignment.accountservice.controller.CustomerRestClient;
import com.assignment.accountservice.dto.AccountDTO;
import com.assignment.accountservice.dto.CustomerDTO;
import com.assignment.accountservice.enums.AccountType;
import com.assignment.accountservice.exception.EntityNotFoundException;
import com.assignment.accountservice.exception.MaxAccountsReachedException;
import com.assignment.accountservice.exception.SalaryAccountExistsException;
import com.assignment.accountservice.model.Account;
import com.assignment.accountservice.repository.AccountRepository;
import com.assignment.accountservice.service.AccountMapper;
import com.assignment.accountservice.service.AccountService;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    private final CustomerRestClient customerRestClient;

    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper, CustomerRestClient customerRestClient) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.customerRestClient = customerRestClient;
    }

    @Override
    @Transactional
    public Account createAccount(AccountDTO accountDTO) {
        logger.info("Creating account: " + accountDTO.toString());
        CustomerDTO customer = getCustomerById(accountDTO.customerId());
        if (accountRepository.countByCustomerId(customer.id()) >= 10)
            throw new MaxAccountsReachedException("Customer already has the maximum number of accounts (10)");

        if (accountDTO.type() == AccountType.SALARY)
            if (accountRepository.existsSalaryAccountForCustomer(customer.id()))
                throw new SalaryAccountExistsException("Customer already has a salary account");

        Account account = accountMapper.fromAccountDTO(accountDTO);
        return accountRepository.save(account);
    }


    @Override
    @Transactional(readOnly = true)
    public Account getAccount(String accountId) {
        logger.info("Getting account with id: " + accountId);
        return accountRepository.findById(accountId).orElseThrow(
                () -> new EntityNotFoundException("Account not found with id '" + accountId + "'.")
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> getCustomerAccounts(String customerId) {
        logger.info("Getting accounts for customer with id: " + customerId);
        return accountRepository.findByCustomerId(customerId);
    }

    @Override
    @Transactional
    public Account updateAccount(String accountId, AccountDTO accountDTO) {
        logger.info("Updating account with id: " + accountId + " to: " + accountDTO.toString());
        Account existingAccount = getAccount(accountId);
        Account newAccount = accountMapper.fromAccountDTO(accountDTO);
        newAccount.setId(existingAccount.getId());
        return accountRepository.save(newAccount);
    }

    @Override
    @Transactional
    public void deleteAccount(String accountId) {
        logger.info("Deleting account with id: " + accountId);
        Account account = getAccount(accountId);
        accountRepository.delete(account);
    }

    @Override
    @Transactional
    public void deleteCustomerAccounts(String customerId) {
        logger.info("Deleting accounts for customer with id: " + customerId);
        List<Account> accounts = getCustomerAccounts(customerId);
        accountRepository.deleteAll(accounts);
    }

    private CustomerDTO getCustomerById(String customerId) {
        try{
            logger.info("Getting customer with id: " + customerId + " from customer-service");
            return customerRestClient.getCustomerById(customerId);
        }catch (FeignException.NotFound e){
            throw new EntityNotFoundException("Customer not found with id '"+customerId+"'.");
        }
    }

    private final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

}
