package com.cashinvoice.assignment.module.order.controller;

import com.cashinvoice.assignment.common.dto.ApiResponse;
import com.cashinvoice.assignment.common.dto.OrderRequestDto;
import com.cashinvoice.assignment.common.dto.OrderResponseDto;
import com.cashinvoice.assignment.common.dto.OrderViewDto;
import com.cashinvoice.assignment.module.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDto>> saveOrder(
            @Valid @RequestBody OrderRequestDto requestDto) {

        OrderResponseDto savedOrder = orderService.placeOrder(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Order placed successfully.", savedOrder));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderViewDto>> getOrderById(
            @PathVariable UUID orderId) {
        OrderViewDto res = orderService.getOrderById(orderId);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Order fetched successfully", res)
        );
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderViewDto>>> getOrdersByCustomer(
            @RequestParam String customerId) {
        List<OrderViewDto> res = orderService.getOrdersByCustomer(customerId);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Customer based orders fetched successfully", res)
        );
    }

}