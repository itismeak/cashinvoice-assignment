package com.cashinvoice.assignment.module.order.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private UUID orderId;
    private String customerId;
    private String product;
    private BigDecimal amount;
    private Instant createdAt;
}