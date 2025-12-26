package com.cashinvoice.assignment.module.order.repository;

import com.cashinvoice.assignment.common.exceptions.ResourceNotFoundException;
import com.cashinvoice.assignment.module.order.entity.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrderStore {
    private List<Order> orders = new ArrayList<>();

    public Order addOrder(Order order) {
        orders.add(order);
        return order;
    }

    public List<Order> getOrdersByCustomer(String customerId) {
        return orders.stream()
                .filter(o -> o.getCustomerId().equals(customerId))
                .toList();
    }

    public Optional<Order> findById(UUID orderId){
        return orders.stream()
                .filter(o -> orderId.equals(o.getOrderId()))
                .findFirst();
    }

}