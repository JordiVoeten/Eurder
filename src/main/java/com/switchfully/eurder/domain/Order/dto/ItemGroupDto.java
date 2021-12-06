package com.switchfully.eurder.domain.Order.dto;

import java.time.LocalDate;

public class ItemGroupDto {
    private String itemId;
    private int amount;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
