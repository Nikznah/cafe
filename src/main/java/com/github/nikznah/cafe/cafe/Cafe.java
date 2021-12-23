package com.github.nikznah.cafe.cafe;

import com.github.nikznah.cafe.Order;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import static com.github.nikznah.cafe.cafe.Barista.State.MAKE_COFFEE;
import static com.github.nikznah.cafe.cafe.Barista.State.WASH_FLOOR;


public class Cafe {
    private final Queue<Order> orders;
    private final ArrayList<Barista> baristas;
    private volatile boolean isOpen;

    public Cafe(ArrayList<Barista> jobShift) {
        this.orders = new LinkedList<>();
        this.isOpen = false;
        this.baristas = new ArrayList<>(jobShift);
    }

    public void putOrder(Order order) {

        orders.add(order);
        System.out.println("Получен новый заказ! №" + order.getOrderNumber());

    }

    public void open() {
        this.isOpen = true;
        new Thread(() -> {
            distributionOfWork();
            while (isOpen) {
                balance();
            }
        }).start();
    }

    private void distributionOfWork() {
        if (baristas.size() <= 0) {
            throw new IllegalArgumentException("В рабочей смене нет людей!");
        }
        baristas.forEach(it -> it.setState(WASH_FLOOR));
        baristas.get(0).setState(MAKE_COFFEE);

        baristas.forEach(it -> new Thread(() -> it.work(orders)).start());
    }

    private void balance() {
        long countMake = baristas.stream()
                .filter(it -> MAKE_COFFEE.equals(it.getState()))
                .count();
        long countWash = baristas.stream()
                .filter(it -> WASH_FLOOR.equals(it.getState()))
                .count();
        if (orders.size() > 10 && orders.size() < 20 && countMake < (baristas.size() - countWash)) {
            goMakeCoffee();
        } else if (orders.size() <= 10 && countMake >= countWash) {
            goWashFloorAll();
        } else if (orders.size() >= 20 && countMake < baristas.size()) {
            goMakeCoffee();
        } else if (orders.size() <= 20 && countMake > (baristas.size() - countWash)) {
            goWashFloor();
        }
    }

    private void goWashFloorAll() {
        List<Barista> tmp = baristas.stream()
                .filter(it -> MAKE_COFFEE.equals(it.getState()))
                .collect(Collectors.toList());
        tmp.remove(0);
        tmp.forEach(it -> it.setState(WASH_FLOOR));
    }

    private void goWashFloor() {
        Barista barista = baristas.stream()
                .filter(it -> MAKE_COFFEE.equals(it.getState()))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Все и там моют полы!"));
        barista.setState(WASH_FLOOR);
    }

    private void goMakeCoffee() {
        Barista barista = baristas.stream()
                .filter(it -> WASH_FLOOR.equals(it.getState()))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Ну всё... мы закопались"));
        barista.setState(MAKE_COFFEE);
    }

}