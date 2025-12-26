package com.cashinvoice.assignment.common.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRequestDto {

    @NotBlank(message = "Customer ID must not be blank")
    private String customerId;

    @NotBlank(message = "Product must not be blank")
    private String product;

    @NotNull(message = "Amount must not be null")
    @Positive(message = "Amount must be greater than 0")
    private BigDecimal amount;
}