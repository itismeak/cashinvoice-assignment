package com.cashinvoice.assignment.module.order.camel;


import com.cashinvoice.assignment.module.order.entity.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FileToQueueRoute extends RouteBuilder {

    private final ObjectMapper objectMapper;

    public FileToQueueRoute(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void configure() {

        JacksonDataFormat jsonFormat = new JacksonDataFormat(Order.class);
        jsonFormat.setObjectMapper(objectMapper);

        onException(Exception.class)
                .handled(true)
                .log("Error processing file: ${header.CamelFileName}")
                .log("Error details: ${exception.message}")
                .to("file:error/orders?autoCreate=true");

        from("file:input/orders?move=../processed&moveFailed=../error&autoCreate=true")
                .routeId("file-to-rabbitmq")
                .log("Processing file: ${header.CamelFileName}")

                .unmarshal(jsonFormat)

                .process(exchange -> {
                    Order order = exchange.getIn().getBody(Order.class);

                    if (order.getOrderId() == null) {
                        throw new IllegalArgumentException("orderId is null");
                    }
                    if (order.getCustomerId() == null) {
                        throw new IllegalArgumentException("customerId is null");
                    }
                    if (order.getAmount() == null || order.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                        throw new IllegalArgumentException("amount must be > 0");
                    }

                    exchange.getIn().setHeader("OrderId", order.getOrderId().toString());
                })

                .log("Valid Order | File=${header.CamelFileName}, OrderId=${header.OrderId}")
                .marshal(jsonFormat)
                //.to("spring-rabbitmq:ORDER.CREATED.QUEUE");
                .to("spring-rabbitmq:ORDER.CREATED.QUEUE?routingKey=ORDER.CREATED.QUEUE&autoDeclare=true");
    }
}