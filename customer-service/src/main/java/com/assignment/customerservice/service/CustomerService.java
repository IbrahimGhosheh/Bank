package com.assignment.customerservice.service;

import com.assignment.customerservice.dto.CustomerDTO;
import com.assignment.customerservice.model.Customer;

import java.util.List;

public interface CustomerService {

    Customer createCustomer(CustomerDTO customer);

    Customer getCustomerById(String customerId);

    List<Customer> getAllCustomers();

    Customer updateCustomer(String customerId, CustomerDTO customer);

    void deleteCustomer(String customerId);
}

