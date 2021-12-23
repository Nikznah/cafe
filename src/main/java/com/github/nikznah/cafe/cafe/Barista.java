package com.github.nikznah.cafe.cafe;

import com.github.nikznah.cafe.Order;
import lombok.*;

import java.util.Queue;

import static com.github.nikznah.cafe.cafe.Barista.State.*;

@Data
public class Barista {


    private String name;
    private int level;

    @Getter
    @Setter
    private volatile State state = REST_HOME;

    @Getter
    @RequiredArgsConstructor
    public enum State {
        MAKE_COFFEE(true),
        WASH_FLOOR(true),
        REST_HOME(false);

        private final boolean working;

    }

    public Barista(String name, int level) {
        this.name = name;
        this.level = level;
    }


    @SneakyThrows
    public void work(Queue<Order> orders) {
        while (state.isWorking()) {
            if (WASH_FLOOR.equals(state)) {
                System.out.println(name + " Моет полы");
                Thread.sleep(5000);
            } else if (MAKE_COFFEE.equals(state)) {
                Order poll = orders.poll();
                if (poll != null) {
                    processOrder(poll);
                } else {
                    System.out.println(name + " Протирает бар");
                    Thread.sleep(2500);
                }
            }
        }
    }

    @SneakyThrows
    public void processOrder(Order order) {
        int makeSpeed;
        switch (level) {
            case 1:
                makeSpeed = 3000;
                break;
            case 2:
                makeSpeed = 2000;
                break;
            case 3:
                makeSpeed = 1000;
                break;
            default:
                makeSpeed = 4000;
        }
        System.out.println(name + " готовит кофе для заказа №" + order.getOrderNumber());
        Thread.sleep(makeSpeed);
        System.out.println(name + " - Заказ №" + order.getOrderNumber() + " готов! ");
    }
}
