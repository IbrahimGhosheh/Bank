package com.assignment.customerservice.service;

import com.assignment.customerservice.dto.CustomerDTO;
import com.assignment.customerservice.model.Customer;

public interface CustomerMapper {
    CustomerDTO fromCustomer(Customer customer);

    Customer fromCustomerDTO(CustomerDTO customerDTO);
}