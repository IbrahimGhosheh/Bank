package com.assignment.customerservice.service.impl;


import com.assignment.customerservice.dto.CustomerDTO;
import com.assignment.customerservice.model.Customer;
import com.assignment.customerservice.model.CustomerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerMapperImplTest {

    private CustomerMapperImpl customerMapper;
    @BeforeEach
    public void setup() {
        customerMapper = new CustomerMapperImpl();
    }

    @Test
    public void test_from_customer_returns_customer_dto_with_same_fields() {
        Customer customer = Customer.builder()
                .name("John Doe")
                .legalId("1234567890")
                .type(CustomerType.CORPORATE)
                .address("123 Main St")
                .build();

        CustomerDTO customerDTO = customerMapper.fromCustomer(customer);

        assertEquals(customer.getName(), customerDTO.name());
        assertEquals(customer.getLegalId(), customerDTO.legalId());
        assertEquals(customer.getType(), customerDTO.type());
        assertEquals(customer.getAddress(), customerDTO.address());
    }

    @Test
    public void test_from_customer_dto_returns_customer_with_same_fields() {
        CustomerDTO customerDTO = new CustomerDTO("John Doe", "1234567890", CustomerType.CORPORATE, "123 Main St");

        Customer customer = customerMapper.fromCustomerDTO(customerDTO);

        assertEquals(customerDTO.name(), customer.getName());
        assertEquals(customerDTO.legalId(), customer.getLegalId());
        assertEquals(customerDTO.type(), customer.getType());
        assertEquals(customerDTO.address(), customer.getAddress());
    }

}