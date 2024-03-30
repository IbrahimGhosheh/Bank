package com.assignment.accountservice.dto;

import com.assignment.accountservice.enums.CustomerType;


public record CustomerDTO(
        String id,
        String name,
        String legalId,
        CustomerType type,
        String address) {
}
