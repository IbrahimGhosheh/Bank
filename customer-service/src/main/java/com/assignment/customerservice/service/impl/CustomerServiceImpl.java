package com.assignment.customerservice.service.impl;

import com.assignment.customerservice.dto.CustomerDTO;
import com.assignment.customerservice.event.DeleteCustomerEvent;
import com.assignment.customerservice.exception.DuplicatedEntityException;
import com.assignment.customerservice.exception.EntityNotFoundException;
import com.assignment.customerservice.model.Customer;
import com.assignment.customerservice.repository.CustomerRepository;
import com.assignment.customerservice.service.CustomerMapper;
import com.assignment.customerservice.service.CustomerService;
import com.assignment.customerservice.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    private final KafkaService kafkaService;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper, KafkaService kafkaService) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.kafkaService = kafkaService;
    }

    @Override
    @Transactional
    public Customer createCustomer(CustomerDTO customer) {
        logger.info("Creating customer: " + customer.toString());
        if(customerRepository.existsByLegalId(customer.legalId()))
            throw new DuplicatedEntityException("Customer with legal id " + customer.legalId() + " already exists");
        return customerRepository.save(
                customerMapper.fromCustomerDTO(customer)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Customer getCustomerById(String customerId) {
        logger.info("Getting customer with id: " + customerId);
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer with id " + customerId + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        logger.info("Getting all customers");
        return customerRepository.findAll();
    }

    @Override
    @Transactional
    public Customer updateCustomer(String customerId, CustomerDTO customer) {
        logger.info("Updating customer with id: " + customerId + " to: " + customer.toString());
        Customer existingCustomer = getCustomerById(customerId);
        Customer newCustomer = customerMapper.fromCustomerDTO(customer);
        newCustomer.setId(existingCustomer.getId());
        return customerRepository.save(newCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomer(String customerId) {
        logger.info("Deleting customer with id: " + customerId);
        Customer existingCustomer = getCustomerById(customerId);
        DeleteCustomerEvent deleteEvent = new DeleteCustomerEvent(customerId);
        kafkaService.sendCustomerEvent(deleteEvent);
        customerRepository.delete(existingCustomer);
    }

    private final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
}
