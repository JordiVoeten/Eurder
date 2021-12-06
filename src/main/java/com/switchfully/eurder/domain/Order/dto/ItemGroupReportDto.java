package com.switchfully.eurder.domain.Order.dto;

import com.switchfully.eurder.domain.item.Currency;
import com.switchfully.eurder.domain.item.Price;

public class ItemGroupReportDto {
    private String itemName;
    private int amount;
    private Price itemGroupPrice;

    public ItemGroupReportDto() {
        this.itemGroupPrice = new Price(0, Currency.EUR);
    }

    public String getItemName() {
        return itemName;
    }

    public ItemGroupReportDto setItemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public ItemGroupReportDto setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public Price getItemGroupPrice() {
        return itemGroupPrice;
    }

    public ItemGroupReportDto setItemGroupPrice(Price itemGroupPrice) {
        this.itemGroupPrice = itemGroupPrice;
        return this;
    }

}
