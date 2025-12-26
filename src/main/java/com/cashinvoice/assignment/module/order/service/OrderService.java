package com.cashinvoice.assignment.module.order.service;

import com.cashinvoice.assignment.common.dto.OrderRequestDto;
import com.cashinvoice.assignment.common.dto.OrderResponseDto;
import com.cashinvoice.assignment.common.dto.OrderViewDto;
import com.cashinvoice.assignment.common.enums.OrderStatus;
import com.cashinvoice.assignment.common.exceptions.ResourceNotFoundException;
import com.cashinvoice.assignment.common.util.OrderIdGenerator;
import com.cashinvoice.assignment.module.order.entity.Order;
import com.cashinvoice.assignment.module.order.repository.OrderStore;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderStore orderStore;
    private final ProducerTemplate producerTemplate;

    public OrderService(OrderStore orderStore,
                        ProducerTemplate producerTemplate
                        ) {
        this.orderStore = orderStore;
        this.producerTemplate = producerTemplate;
    }


    public OrderResponseDto placeOrder(OrderRequestDto request){
        Order order=new Order();
        order.setCustomerId(request.getCustomerId());
        order.setProduct(request.getProduct());
        order.setAmount(request.getAmount());

        order.setOrderId(OrderIdGenerator.generateOrderId());
        order.setCreatedAt(Instant.now());
        Order savedOrder = orderStore.addOrder(order);

        producerTemplate.sendBody("direct:write-order-file", order);

        OrderResponseDto res=new OrderResponseDto();
        res.setOrderId(savedOrder.getOrderId());
        res.setStatus(OrderStatus.CREATED);
        return res;
    }

    public List<OrderViewDto> getOrdersByCustomer(String customerId){
        List<Order> orders = orderStore.getOrdersByCustomer(customerId);
        if(orders.isEmpty()){
            throw new ResourceNotFoundException("No customer found: "+ customerId);
        }
        List<OrderViewDto> ordersByCustomer = orders.stream()
                .map(d -> acceptOrder(d))
                .toList();
        return ordersByCustomer;
    }

    public OrderViewDto getOrderById(UUID orderId){
        Optional<Order> order=orderStore.findById(orderId);
        if(order.isEmpty()){
            throw new ResourceNotFoundException("Order not found: "+orderId.toString());
        }
        return acceptOrder(order.get());
    }

    private OrderViewDto acceptOrder(Order order) {
        OrderViewDto dto = new OrderViewDto();
        dto.setOrderId(order.getOrderId());
        dto.setCustomerId(order.getCustomerId());
        dto.setProduct(order.getProduct());
        dto.setAmount(order.getAmount());
        dto.setCreatedAt(order.getCreatedAt());
        return dto;
    }

}