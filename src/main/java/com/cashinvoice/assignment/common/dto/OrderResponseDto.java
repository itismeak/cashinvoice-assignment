package com.cashinvoice.assignment.common.dto;

import com.cashinvoice.assignment.common.enums.OrderStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class OrderResponseDto {
    private UUID orderId;
    private OrderStatus status;
}