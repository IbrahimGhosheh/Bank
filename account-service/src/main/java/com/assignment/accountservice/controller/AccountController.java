package com.assignment.accountservice.controller;

import com.assignment.accountservice.dto.AccountDTO;
import com.assignment.accountservice.model.Account;
import com.assignment.accountservice.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@Tag(name = "Accounts", description = "Operations related to accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @Operation(summary = "Create a new account")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountDTO account) {
        return new ResponseEntity<>(
                accountService.createAccount(account),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{accountId}")
    @Operation(summary = "Get an account by ID")
    public ResponseEntity<Account> getAccountById(@PathVariable String accountId) {
        return ResponseEntity.ok(
                accountService.getAccount(accountId)
        );
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get all accounts for a customer")
    public ResponseEntity<List<Account>> getAccountsByCustomerId(@PathVariable String customerId) {
        return ResponseEntity.ok(
                accountService.getCustomerAccounts(customerId)
        );
    }

    @PutMapping("/{accountId}")
    @Operation(summary = "Update an account by ID")
    public ResponseEntity<Account> updateAccount(@PathVariable String accountId, @Valid @RequestBody AccountDTO account) {
        return ResponseEntity.ok(
                accountService.updateAccount(accountId, account)
        );
    }

    @DeleteMapping("/{accountId}")
    @Operation(summary = "Delete an account by ID")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/customer/{customerId}")
    @Operation(summary = "Delete all accounts for a customer")
    public ResponseEntity<Void> deleteCustomerAccounts(@PathVariable String customerId) {
        accountService.deleteCustomerAccounts(customerId);
        return ResponseEntity.noContent().build();
    }
}
