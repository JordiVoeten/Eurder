package com.switchfully.eurder.domain.item;

import java.util.UUID;

public class Item {
    private final String id;
    private final String name;
    private final String description;
    private final Price price;
    private final int amount;

    public Item(String name, String description, Price price, int amount) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Price getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }
}
