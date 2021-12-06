package com.switchfully.eurder.repository;

import com.switchfully.eurder.domain.Order.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepository {
    public List<Order> orderList;

    public OrderRepository() {
        this.orderList = new ArrayList<>();
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public Order addOrder(Order order) {
        orderList.add(order);
        return order;
    }
}
