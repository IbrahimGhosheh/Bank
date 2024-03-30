package com.assignment.accountservice.service.impl;

import com.assignment.accountservice.controller.CustomerRestClient;
import com.assignment.accountservice.dto.AccountDTO;
import com.assignment.accountservice.dto.CustomerDTO;
import com.assignment.accountservice.enums.AccountStatus;
import com.assignment.accountservice.enums.AccountType;
import com.assignment.accountservice.enums.Currency;
import com.assignment.accountservice.enums.CustomerType;
import com.assignment.accountservice.exception.EntityNotFoundException;
import com.assignment.accountservice.exception.MaxAccountsReachedException;
import com.assignment.accountservice.exception.SalaryAccountExistsException;
import com.assignment.accountservice.model.Account;
import com.assignment.accountservice.repository.AccountRepository;
import com.assignment.accountservice.service.AccountMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
public class AccountServiceImplTest {

    private AccountRepository accountRepository;
    private AccountMapper accountMapper;
    private CustomerRestClient customerRestClient;
    private AccountServiceImpl accountService;

    @BeforeEach
    public void setup() {
        accountRepository = mock(AccountRepository.class);
        accountMapper = mock(AccountMapper.class);
        customerRestClient = mock(CustomerRestClient.class);
        accountService = new AccountServiceImpl(accountRepository, accountMapper, customerRestClient);
    }

    @Test
    public void test_createAccount_withValidAccountDTO_shouldSaveAccountAndReturnIt() {
        AccountDTO accountDTO = new AccountDTO("customerId", BigDecimal.valueOf(1000), Currency.USD, AccountStatus.ACTIVATED, AccountType.SAVING);
        Account account = new Account("accountId", "customerId", BigDecimal.valueOf(100), Currency.USD, AccountStatus.ACTIVATED, AccountType.SAVING);
        when(customerRestClient.getCustomerById(accountDTO.customerId())).thenReturn(new CustomerDTO("customerId", "John Doe", "1234567890", CustomerType.RETAIL, "123 Main St"));
        when(accountMapper.fromAccountDTO(accountDTO)).thenReturn(account);
        when(accountRepository.countByCustomerId(accountDTO.customerId())).thenReturn(5L);
        when(accountRepository.save(account)).thenReturn(account);

        Account result = accountService.createAccount(accountDTO);


        assertEquals(account, result);
        verify(accountRepository, times(1)).countByCustomerId(accountDTO.customerId());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    public void test_getAccount_withValidAccountId_shouldReturnAccount() {
        String accountId = "accountId";
        Account account = new Account("accountId", "customerId", BigDecimal.valueOf(1000), Currency.USD, AccountStatus.ACTIVATED, AccountType.SAVING);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        Account result = accountService.getAccount(accountId);

        assertEquals(account, result);
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    public void test_getCustomerAccounts_withValidCustomerId_shouldReturnListOfAccounts() {
        String customerId = "customerId";
        List<Account> accounts = List.of(
                new Account("accountId1", customerId, BigDecimal.valueOf(1000), Currency.USD, AccountStatus.ACTIVATED, AccountType.SAVING),
                new Account("accountId2", customerId, BigDecimal.valueOf(2000), Currency.USD, AccountStatus.ACTIVATED, AccountType.SAVING)
        );
        when(accountRepository.findByCustomerId(customerId)).thenReturn(accounts);

        List<Account> result = accountService.getCustomerAccounts(customerId);

        assertEquals(accounts, result);
        verify(accountRepository, times(1)).findByCustomerId(customerId);
    }

    @Test
    public void test_deletes_existing_account() {
        String accountId = "123";
        Account account = new Account();
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        accountService.deleteAccount(accountId);

        verify(accountRepository).delete(account);
    }

    @Test
    public void test_delete_all_customer_accounts() {
        String customerId = "1234567890";
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account("1", customerId, BigDecimal.valueOf(1000), Currency.USD, AccountStatus.ACTIVATED, AccountType.SAVING));
        accounts.add(new Account("2", customerId, BigDecimal.valueOf(2000), Currency.USD, AccountStatus.ACTIVATED, AccountType.SAVING));
        accounts.add(new Account("3", customerId, BigDecimal.valueOf(3000), Currency.USD, AccountStatus.ACTIVATED, AccountType.INVESTMENT));
        when(accountRepository.findByCustomerId(customerId)).thenReturn(accounts);

        accountService.deleteCustomerAccounts(customerId);

        verify(accountRepository, times(1)).deleteAll(accounts);
    }

    @Test
    public void test_updateAccount_withValidAccountIdAndAccountDTO_shouldUpdateAccountAndReturnIt() {
        String accountId = "accountId";
        AccountDTO accountDTO = new AccountDTO("customerId", BigDecimal.valueOf(2000), Currency.USD, AccountStatus.ACTIVATED, AccountType.SAVING);
        Account existingAccount = new Account(accountId, "customerId", BigDecimal.valueOf(1000), Currency.USD, AccountStatus.ACTIVATED, AccountType.SAVING);
        Account updatedAccount = new Account(accountId, "customerId", BigDecimal.valueOf(2000), Currency.USD, AccountStatus.ACTIVATED, AccountType.SAVING);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
        when(accountMapper.fromAccountDTO(accountDTO)).thenReturn(updatedAccount);
        when(accountRepository.save(updatedAccount)).thenReturn(updatedAccount);

        Account result = accountService.updateAccount(accountId, accountDTO);

        assertEquals(updatedAccount, result);
        verify(accountRepository, times(1)).findById(accountId);
        verify(accountRepository, times(1)).save(updatedAccount);
    }

    @Test
    public void test_createAccount_withInvalidCustomerId_shouldThrowEntityNotFoundException() {
        AccountDTO accountDTO = new AccountDTO("invalidCustomerId", BigDecimal.valueOf(1000), Currency.USD, AccountStatus.ACTIVATED, AccountType.SAVING);
        when(customerRestClient.getCustomerById(accountDTO.customerId())).thenThrow(new EntityNotFoundException("Customer not found with id 'invalidCustomerId'."));

        assertThrows(EntityNotFoundException.class, () -> accountService.createAccount(accountDTO));
        verify(customerRestClient, times(1)).getCustomerById(accountDTO.customerId());
    }

    @Test
    public void test_createAccount_withCustomerIdAlreadyHaving10Accounts_shouldThrowMaxAccountsReachedException() {
        AccountDTO accountDTO = new AccountDTO("customerId", BigDecimal.valueOf(1000), Currency.USD, AccountStatus.ACTIVATED, AccountType.SAVING);
        when(customerRestClient.getCustomerById(accountDTO.customerId())).thenReturn(new CustomerDTO("customerId", "John Doe", "1234567890", CustomerType.RETAIL, "123 Main St"));
        when(accountRepository.countByCustomerId(accountDTO.customerId())).thenReturn(10L);

        assertThrows(MaxAccountsReachedException.class, () -> accountService.createAccount(accountDTO));
        verify(accountRepository, times(1)).countByCustomerId(accountDTO.customerId());
    }

    @Test
    public void test_createAccount_withCustomerIdAlreadyHavingSalaryAccount_shouldThrowSalaryAccountExistsException() {
        AccountDTO accountDTO = new AccountDTO("customerId", BigDecimal.valueOf(1000), Currency.USD, AccountStatus.ACTIVATED, AccountType.SALARY);
        when(customerRestClient.getCustomerById(accountDTO.customerId())).thenReturn(new CustomerDTO("customerId", "John Doe", "1234567890", CustomerType.RETAIL, "123 Main St"));
        when(accountRepository.existsSalaryAccountForCustomer(accountDTO.customerId())).thenReturn(true);

        assertThrows(SalaryAccountExistsException.class, () -> accountService.createAccount(accountDTO));
        verify(accountRepository, times(1)).existsSalaryAccountForCustomer(accountDTO.customerId());
    }

    @Test
    public void test_throws_entity_not_found_exception_if_account_not_exists() {
        String accountId = "123";
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> accountService.getAccount(accountId));
        verify(accountRepository, times(1)).findById(accountId);
    }

}