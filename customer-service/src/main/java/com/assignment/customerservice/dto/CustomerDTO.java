package com.assignment.customerservice.dto;

import com.assignment.customerservice.model.CustomerType;
import jakarta.validation.constraints.NotNull;


public record CustomerDTO(
        @NotNull
        String name,
        @NotNull
        String legalId,
        @NotNull
        CustomerType type,
        @NotNull
        String address) {
}
