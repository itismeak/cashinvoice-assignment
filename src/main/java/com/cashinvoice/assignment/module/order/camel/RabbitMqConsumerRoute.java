package com.cashinvoice.assignment.module.order.camel;

import com.cashinvoice.assignment.module.order.entity.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqConsumerRoute extends RouteBuilder {

    private final ObjectMapper objectMapper;

    public RabbitMqConsumerRoute(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void configure() {

        JacksonDataFormat jsonFormat = new JacksonDataFormat(Order.class);
        jsonFormat.setObjectMapper(objectMapper);

        onException(Exception.class)
                .handled(true)
                .log("Error processing RabbitMQ message: ${exception.message}")
                .to("log:rabbitmq-error");

        from("spring-rabbitmq:ORDER.CREATED.QUEUE?routingKey=ORDER.CREATED.QUEUE&autoDeclare=true")
                .routeId("rabbitmq-consumer")
                .process(exchange -> {
                    String json = exchange.getIn().getBody(String.class);
                    Order order = objectMapper.readValue(json, Order.class);

                    log.info(
                            "Order processed ----> OrderId={} | CustomerId={} | Amount={}",
                            order.getOrderId(),
                            order.getCustomerId(),
                            order.getAmount()
                    );
                });

    }
}