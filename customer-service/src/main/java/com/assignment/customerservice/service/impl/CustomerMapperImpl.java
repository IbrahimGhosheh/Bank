package com.assignment.customerservice.service.impl;

import com.assignment.customerservice.dto.CustomerDTO;
import com.assignment.customerservice.model.Customer;
import com.assignment.customerservice.service.CustomerMapper;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapperImpl implements CustomerMapper {
    @Override
    public CustomerDTO fromCustomer(Customer customer) {
        return new CustomerDTO(
                customer.getName(),
                customer.getLegalId(),
                customer.getType(),
                customer.getAddress()
        );
    }

    @Override
    public Customer fromCustomerDTO(CustomerDTO customerDTO) {
        return Customer.builder()
                .name(customerDTO.name())
                .legalId(customerDTO.legalId())
                .type(customerDTO.type())
                .address(customerDTO.address())
                .build();
    }
}
