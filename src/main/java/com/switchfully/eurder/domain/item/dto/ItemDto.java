package com.switchfully.eurder.domain.item.dto;

import com.switchfully.eurder.domain.item.Price;

public class ItemDto {
    private String id;
    private String name;
    private String description;
    private Price price;
    private int amount;

    public String getId() {
        return id;
    }

    public ItemDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ItemDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ItemDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public Price getPrice() {
        return price;
    }

    public ItemDto setPrice(Price price) {
        this.price = price;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public ItemDto setAmount(int amount) {
        this.amount = amount;
        return this;
    }
}
