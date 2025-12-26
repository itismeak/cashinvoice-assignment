package com.cashinvoice.assignment.module.order.controller;

import com.cashinvoice.assignment.common.dto.ApiResponse;
import com.cashinvoice.assignment.common.dto.OrderRequestDto;
import com.cashinvoice.assignment.common.dto.OrderResponseDto;
import com.cashinvoice.assignment.common.dto.OrderViewDto;
import com.cashinvoice.assignment.common.exceptions.BadRequestException;
import com.cashinvoice.assignment.module.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
            @PathVariable UUID orderId, Authentication authentication) {

        OrderViewDto res = orderService.getOrderById(orderId);

        // ADMIN can view all orders
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Order fetched successfully", res)
            );
        }

        // USER can only view their own orders
        if (!res.getCustomerId().equals(authentication.getName())) {
            throw new BadRequestException(
                    "You are not allowed to access this order",
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Order fetched successfully", res)
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<OrderViewDto>>> getOrdersByCustomer(
            @RequestParam String customerId) {
        List<OrderViewDto> res = orderService.getOrdersByCustomer(customerId);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Customer based orders fetched successfully", res)
        );
    }

}