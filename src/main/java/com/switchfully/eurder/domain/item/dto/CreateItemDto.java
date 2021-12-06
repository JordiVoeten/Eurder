package com.switchfully.eurder.domain.item.dto;

import com.switchfully.eurder.domain.item.Price;

public class CreateItemDto {
    private String name;
    private String description;
    private Price price;
    private int amount;

    public String getName() {
        return name;
    }

    public CreateItemDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CreateItemDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public Price getPrice() {
        return price;
    }

    public CreateItemDto setPrice(Price price) {
        this.price = price;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public CreateItemDto setAmount(int amount) {
        this.amount = amount;
        return this;
    }
}
