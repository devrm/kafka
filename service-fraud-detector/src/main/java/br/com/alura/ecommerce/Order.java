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

    public boolean isFraud() {
        return ammount.compareTo(new BigDecimal("4500")) >= 0;
    }


    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "userId='" + userId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", ammount=" + ammount +
                '}';
    }
}
