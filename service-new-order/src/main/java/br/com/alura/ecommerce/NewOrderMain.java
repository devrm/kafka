package br.com.alura.ecommerce;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {


    public static void main(String ...args) throws ExecutionException, InterruptedException {
        try (KafkaDispatcher dispatcher = new KafkaDispatcher<Order>()) {
            for (int i = 0; i < 10; i++) {
                String userId = UUID.randomUUID().toString();
                String orderId = UUID.randomUUID().toString();
                final BigDecimal ammount = new BigDecimal(Math.random() * 5000 + 1);

                Order order = new Order(userId, orderId, ammount);
                Email email = new Email("New order", "Thank you! Processing your new order.");
                dispatcher.send("ECOMMERCE_NEW_ORDER", userId, order);
                dispatcher.send("ECOMMERCE_SEND_EMAIL", userId, "Thank you! Processing your new order.");

            }
        }
    }

}
