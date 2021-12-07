package com.switchfully.eurder.domain.item.dto;

import com.switchfully.eurder.domain.item.Price;

public class UpdateItemDto {
    private String name;
    private String description;
    private Price price;
    private int amount;


    public String getName() {
        return name;
    }

    public UpdateItemDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public UpdateItemDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public Price getPrice() {
        return price;
    }

    public UpdateItemDto setPrice(Price price) {
        this.price = price;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public UpdateItemDto setAmount(int amount) {
        this.amount = amount;
        return this;
    }
}

