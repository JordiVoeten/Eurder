package com.switchfully.eurder.domain.Order;

import com.switchfully.eurder.domain.item.Price;

import java.util.List;
import java.util.UUID;

public class Order {
    private final String id;
    private List<ItemGroup> itemGroups;
    private String customerId;
    private Price totalPrice;

    public Order(List<ItemGroup> itemGroups, String customerId) {
        this.id = UUID.randomUUID().toString();
        this.itemGroups = itemGroups;
        this.customerId = customerId;
    }

    public void setTotalPrice(Price totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getId() {
        return id;
    }

    public List<ItemGroup> getItemGroups() {
        return itemGroups;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Price getTotalPrice() {
        return totalPrice;
    }
}
