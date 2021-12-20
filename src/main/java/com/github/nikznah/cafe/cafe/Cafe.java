package com.github.nikznah.cafe.cafe;

import com.github.nikznah.cafe.Order;
import lombok.Data;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

@Data
public class Cafe {
    private final ConcurrentLinkedQueue<Order> orders;
    private final ArrayList<Barista> baristas = new ArrayList<>();
    private volatile boolean isOpen;

    public Cafe() {
        this.orders = new ConcurrentLinkedQueue<>();
    }

    public void putOrder(Order order) {

        orders.add(order);
        System.out.println("Получен новый заказ! №" + order.getOrderNumber());

    }

    public void open(ArrayList<Barista> baristas) {
        this.isOpen = true;
        distributionOfWork(baristas);
    }

    private void distributionOfWork(ArrayList<Barista> baristas) {
        if (baristas.size() <= 0) {
            throw new IllegalArgumentException("В рабочей смене нет людей!");
        } else {
            for (Barista barista : baristas) {
                if (barista.getLevel() == 1) {
                    baristaWork(isOpen, !orders.isEmpty(), barista, orders, 3000);
                } else if (barista.getLevel() == 2) {
                    baristaWork(isOpen, orders.size() > 10, barista, orders, 2000);
                } else if (barista.getLevel() == 3) {
                    baristaWork(isOpen, orders.size() > 20, barista, orders, 1000);
                }
            }
        }
    }

    private void distributionOfWork(Barista barista, ConcurrentLinkedQueue<Order> orders) {
        if (barista == null) {
            throw new IllegalArgumentException("Не осталось свободных барист!");
        } else {
            if (barista.getLevel() == 1) {
                baristaWork(isOpen, !orders.isEmpty(), barista, orders, 3000);
            } else if (barista.getLevel() == 2) {
                baristaWork(isOpen, orders.size() > 10, barista, orders, 2000);
            } else if (barista.getLevel() == 3) {
                baristaWork(isOpen, orders.size() > 20, barista, orders, 1000);
            }
        }
    }

    private void baristaWork(boolean isopen, boolean orders, Barista barista, ConcurrentLinkedQueue<Order> orders1, int speed) {
        new Thread(() -> {
            boolean check = true;
            while (isopen && check) {
                if (orders) {
                    barista.processOrder(Objects.requireNonNull(orders1.poll()), speed);
                } else {
                    System.out.println(barista.getName() + " моет пол," +
                            "Кол-во активных заказов =" + orders1.size());
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    check = false;
                }
            }
            if (!check) {
                distributionOfWork(barista, orders1);
            }
        }).start();
    }
}
