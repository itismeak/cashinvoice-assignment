package com.cashinvoice.assignment.common.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
public class OrderViewDto {
    private UUID orderId;
    private String customerId;
    private String product;
    private BigDecimal amount;
    private Instant createdAt;
}