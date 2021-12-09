package com.switchfully.eurder.domain.item.dto;

import com.switchfully.eurder.domain.item.Price;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateItemDto {
    private String name;
    private String description;
    private Price price;
    private int amount;

    public UpdateItemDto() {
    }

    public UpdateItemDto(String name, String description, Price price, int amount) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
    }
}

