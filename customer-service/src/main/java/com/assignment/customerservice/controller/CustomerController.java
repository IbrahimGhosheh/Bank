package com.assignment.customerservice.controller;

import com.assignment.customerservice.dto.CustomerDTO;
import com.assignment.customerservice.model.Customer;
import com.assignment.customerservice.service.CustomerService;
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
@RequestMapping("/customers")
@Tag(name = "Customers", description = "Operations related to customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @Operation(summary = "Create a new customer")
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody CustomerDTO customer) {
        return new ResponseEntity<>(
                customerService.createCustomer(customer),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{customerId}")
    @Operation(summary = "Get a customer by ID")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String customerId) {
        return ResponseEntity.ok(
                customerService.getCustomerById(customerId)
        );
    }

    @GetMapping
    @Operation(summary = "Get all customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(
                customerService.getAllCustomers()
        );
    }

    @PutMapping("/{customerId}")
    @Operation(summary = "Update a customer by ID")
    public ResponseEntity<Customer> updateCustomer(@PathVariable String customerId, @Valid @RequestBody CustomerDTO customer) {
        return ResponseEntity.ok(
                customerService.updateCustomer(customerId, customer)
        );
    }

    @DeleteMapping("/{customerId}")
    @Operation(summary = "Delete a customer by ID")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
}

