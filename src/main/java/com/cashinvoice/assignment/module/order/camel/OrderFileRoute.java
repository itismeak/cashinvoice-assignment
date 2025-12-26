package com.cashinvoice.assignment.module.order.camel;

import com.cashinvoice.assignment.module.order.entity.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.stereotype.Component;

@Component
public class OrderFileRoute extends RouteBuilder {

    private final ObjectMapper objectMapper;

    public OrderFileRoute(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void configure() {

        JacksonDataFormat jsonFormat = new JacksonDataFormat(Order.class);
        jsonFormat.setObjectMapper(objectMapper);

        from("direct:write-order-file")
                .routeId("order-to-file")
                .process(exchange -> {
                    Order order = exchange.getIn().getBody(Order.class);
                    exchange.getIn().setHeader(
                            Exchange.FILE_NAME,
                            "order-" + order.getOrderId() + ".json"
                    );
                })
                .marshal(jsonFormat)
                .to("file:input/orders?autoCreate=true")
                .log("Order file created: ${header.CamelFileName}");
    }
}