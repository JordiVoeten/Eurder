package com.switchfully.eurder.domain.item.dto;

import com.switchfully.eurder.domain.item.Price;
import com.switchfully.eurder.domain.item.StockLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ItemDto {
    private String id;
    private StockLevel stockLevel;
    private String name;
    private String description;
    private Price price;
    private int amount;

    public ItemDto() {
    }

    public ItemDto(String id, StockLevel stockLevel, String name, String description, Price price, int amount) {
        this.id = id;
        this.stockLevel = stockLevel;
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
    }
}
