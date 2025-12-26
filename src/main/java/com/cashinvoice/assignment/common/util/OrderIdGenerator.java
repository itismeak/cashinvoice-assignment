package com.cashinvoice.assignment.common.util;

import java.util.UUID;

public final class OrderIdGenerator {
    private OrderIdGenerator() {}

    public static UUID generateOrderId() {
        return UUID.randomUUID();
    }
}