package com.github.nikznah.cafe;

import com.github.nikznah.cafe.cafe.Barista;
import com.github.nikznah.cafe.cafe.Cafe;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Application {
    private static final AtomicInteger ORDER_NUMBER_SEQ = new AtomicInteger();

    @SneakyThrows
    public static void main(String[] args) {

        Barista barista1 = new Barista("Никита", 3);
        Barista barista2 = new Barista("Петя", 2);
        Barista barista3 = new Barista("Александр", 1);

        ArrayList<Barista> jobShift = new ArrayList<>();

        jobShift.add(barista1);
        jobShift.add(barista2);
        jobShift.add(barista3);

        Cafe cafe = new Cafe();

        Runnable customer = () -> {
            for (int i = 0; i < 100; i++) {
                cafe.putOrder(new Order(ORDER_NUMBER_SEQ.incrementAndGet() ));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(customer).start();
        new Thread(customer).start();

        Thread.sleep(10000);
        cafe.open(jobShift);
    }
}
