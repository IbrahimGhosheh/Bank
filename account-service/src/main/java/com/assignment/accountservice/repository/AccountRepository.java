package com.assignment.accountservice.repository;

import com.assignment.accountservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {

    List<Account> findByCustomerId(String customerId);

    Long countByCustomerId(String customerId);

    @Query("SELECT COUNT(a) > 0 FROM Account a WHERE a.customerId = :customerId AND a.type = 'SALARY'")
    boolean existsSalaryAccountForCustomer(String customerId);
}