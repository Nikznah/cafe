package com.github.nikznah.cafe;

import com.github.nikznah.cafe.cafe.Barista;
import com.github.nikznah.cafe.cafe.Cafe;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Application {
    private static final AtomicInteger ORDER_NUMBER_SEQ = new AtomicInteger();
    private static final Random RANDOM = new Random();

    @SneakyThrows
    public static void main(String[] args) {

        Barista barista1 = new Barista("Никита", 3);
        Barista barista2 = new Barista("Гоша", 2);
        Barista barista3 = new Barista("Кирилл1", 1);
        Barista barista4 = new Barista("Кирилл2", 1);
        Barista barista5 = new Barista("Кирилл3", 1);
        Barista barista6 = new Barista("Кирилл4", 1);
        Barista barista7 = new Barista("Кирилл5", 1);
        Barista barista8 = new Barista("Кирилл6", 1);
        Barista barista9 = new Barista("Кирилл7", 1);
        Barista barista10 = new Barista("Кирилл8", 1);
        Barista barista11 = new Barista("Никита2", 3);

        ArrayList<Barista> jobShift = new ArrayList<>();
        jobShift.add(barista1);
        jobShift.add(barista2);
        jobShift.add(barista3);
        jobShift.add(barista4);
        jobShift.add(barista5);
        jobShift.add(barista6);
        jobShift.add(barista7);
        jobShift.add(barista8);
        jobShift.add(barista9);
        jobShift.add(barista10);
        jobShift.add(barista11);

        Cafe cafe = new Cafe(jobShift);

        Runnable customer = () -> {
            for (int i = 0; i < 100; i++) {
                cafe.putOrder(new Order(ORDER_NUMBER_SEQ.incrementAndGet()));
                try {
                    Thread.sleep(RANDOM.nextInt(checkTime()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(customer).start();
        new Thread(customer).start();

        cafe.open();
    }

    private static int checkTime() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm");
        String format = simpleDateFormat.format(date);
        int temp = Integer.parseInt(format);
        if (temp % 2 == 0) {
            return 500;
        } else {
            return 5000;
        }
    }
}
