package com.switchfully.eurder.domain.Order;

import com.switchfully.eurder.domain.item.Item;

import java.time.LocalDate;

public class ItemGroup {
    public static final int DELIVERY_TIME_ITEM_NOT_IN_STOCK = 7;
    public static final int DELIVERY_TIME_ITEM_IN_STOCK = 1;

    private String itemId;
    private int amount;
    private LocalDate shippingDate;

    public ItemGroup(Item item, int amount) {
        this.itemId = item.getId();
        this.amount = amount;
        setShippingDate(item, amount);
    }

    private void setShippingDate(Item item, int amount) {
        shippingDate = LocalDate.now();
        if (amount > item.getAmount()) {
            this.shippingDate = shippingDate.plusDays(DELIVERY_TIME_ITEM_NOT_IN_STOCK);
        } else {
            this.shippingDate = shippingDate.plusDays(DELIVERY_TIME_ITEM_IN_STOCK);
        }
    }

    public String getItemId() {
        return itemId;
    }

    public int getAmount() {
        return amount;
    }

    public LocalDate getShippingDate() {
        return shippingDate;
    }


}
