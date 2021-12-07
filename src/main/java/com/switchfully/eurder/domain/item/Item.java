package com.switchfully.eurder.domain.item;

import java.util.UUID;

public class Item {
    private final String id;
    private String name;
    private String description;
    private Price price;
    private int amount;

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

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
