package br.com.alura.ecommerce;

import java.math.BigDecimal;

public class Order {


    private final String userId, orderId;
    private BigDecimal ammount;


    public Order(String userId, String orderId, BigDecimal ammount) {
        this.userId = userId;
        this.orderId = orderId;
        this.ammount = ammount;
    }
}
