package com.assignment.customerservice.service.impl;

import com.assignment.customerservice.dto.CustomerDTO;
import com.assignment.customerservice.event.DeleteCustomerEvent;
import com.assignment.customerservice.exception.DuplicatedEntityException;
import com.assignment.customerservice.exception.EntityNotFoundException;
import com.assignment.customerservice.model.Customer;
import com.assignment.customerservice.model.CustomerType;
import com.assignment.customerservice.repository.CustomerRepository;
import com.assignment.customerservice.service.CustomerMapper;
import com.assignment.customerservice.service.KafkaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class CustomerServiceImplTest {

    private CustomerRepository customerRepository;
    private CustomerMapper customerMapper;
    private KafkaService kafkaService;
    private CustomerServiceImpl customerService;

    @BeforeEach
    public void setup() {
        customerRepository = mock(CustomerRepository.class);
        customerMapper = mock(CustomerMapper.class);
        kafkaService = mock(KafkaService.class);
        customerService = new CustomerServiceImpl(customerRepository, customerMapper, kafkaService);
    }


    @Test
    public void test_createCustomer_withValidData() {
        CustomerDTO customer = new CustomerDTO("John Doe", "1234567890", CustomerType.CORPORATE, "123 Main St");

        when(customerRepository.existsByLegalId(customer.legalId())).thenReturn(false);
        when(customerMapper.fromCustomerDTO(customer)).thenReturn(new Customer("1", "John Doe", "1234567890", CustomerType.CORPORATE, "123 Main St"));
        when(customerRepository.save(any(Customer.class))).thenReturn(new Customer("1", "John Doe", "1234567890", CustomerType.CORPORATE, "123 Main St"));

        Customer createdCustomer = customerService.createCustomer(customer);

        assertEquals("1", createdCustomer.getId());
        assertEquals("John Doe", createdCustomer.getName());
        assertEquals("1234567890", createdCustomer.getLegalId());
        assertEquals(CustomerType.CORPORATE, createdCustomer.getType());
        assertEquals("123 Main St", createdCustomer.getAddress());
    }

    // Retrieving an existing customer by ID should return the customer
    @Test
    public void test_getCustomerById_withExistingCustomerId() {
        String customerId = "1";
        Customer existingCustomer = new Customer("1", "John Doe", "1234567890", CustomerType.CORPORATE, "123 Main St");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));

        Customer retrievedCustomer = customerService.getCustomerById(customerId);

        assertEquals(existingCustomer, retrievedCustomer);
    }

    @Test
    public void test_getAllCustomers() {
        List<Customer> customers = List.of(
                new Customer("1", "John Doe", "1234567890", CustomerType.CORPORATE, "123 Main St"),
                new Customer("2", "Jane Smith", "0987654321", CustomerType.INVESTMENT, "456 Park Ave")
        );

        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> allCustomers = customerService.getAllCustomers();

        assertEquals(customers, allCustomers);
    }

    @Test
    public void test_updateCustomer_withExistingCustomerIdAndValidData() {
        String customerId = "1";
        Customer existingCustomer = new Customer("1", "John Doe", "1234567890", CustomerType.RETAIL, "123 Main St");
        CustomerDTO updatedCustomerDTO = new CustomerDTO("Jane Smith", "0987654321", CustomerType.RETAIL, "456 Park Ave");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerMapper.fromCustomerDTO(updatedCustomerDTO)).thenReturn(new Customer("1", "Jane Smith", "0987654321", CustomerType.RETAIL, "456 Park Ave"));
        when(customerRepository.save(any(Customer.class))).thenReturn(new Customer("1", "Jane Smith", "0987654321", CustomerType.RETAIL, "456 Park Ave"));

        Customer updatedCustomer = customerService.updateCustomer(customerId, updatedCustomerDTO);

        assertEquals("1", updatedCustomer.getId());
        assertEquals("Jane Smith", updatedCustomer.getName());
        assertEquals("0987654321", updatedCustomer.getLegalId());
        assertEquals(CustomerType.RETAIL, updatedCustomer.getType());
        assertEquals("456 Park Ave", updatedCustomer.getAddress());
    }

    @Test
    public void test_createCustomer_withExistingLegalId() {
        CustomerDTO customer = new CustomerDTO("John Doe", "1234567890", CustomerType.RETAIL, "123 Main St");

        when(customerRepository.existsByLegalId(customer.legalId())).thenReturn(true);

        assertThrows(DuplicatedEntityException.class, () -> customerService.createCustomer(customer));
    }

    @Test
    public void test_getCustomerById_withNonExistentCustomerId() {
        String customerId = "1";

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> customerService.getCustomerById(customerId));
    }

    @Test
    public void test_updateCustomer_withNonExistentCustomerId() {
        String customerId = "1";
        CustomerDTO updatedCustomerDTO = new CustomerDTO("Jane Smith", "0987654321", CustomerType.INVESTMENT, "456 Park Ave");

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> customerService.updateCustomer(customerId, updatedCustomerDTO));
    }

    @Test
    public void test_deleteCustomer_withNonExistentCustomerId() {
        String customerId = "1";

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> customerService.deleteCustomer(customerId));
    }

    @Test
    public void test_deleteCustomer_sendsDeleteEventToKafka() {
        String customerId = "validCustomerId";
        Customer existingCustomer = new Customer();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));

        customerService.deleteCustomer(customerId);

        verify(kafkaService, times(1)).sendCustomerEvent(any(DeleteCustomerEvent.class));
    }

}