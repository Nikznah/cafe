package com.github.nikznah.cafe.cafe;

import com.github.nikznah.cafe.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Barista {

    private String name;
    private int level;

    public void processOrder(Order order,Integer makingSpeed) {
        System.out.println(name +" готовит кофе для заказа №" + order.getOrderNumber());
        try {
            Thread.sleep(makingSpeed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name + " - Заказ №" + order.getOrderNumber() + " готов! ");
    }
}
