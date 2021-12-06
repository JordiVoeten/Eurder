package com.switchfully.eurder.domain.Order;

import com.switchfully.eurder.domain.item.Item;
import com.switchfully.eurder.domain.item.Price;

import java.time.LocalDate;

public class ItemGroup {
    public static final int DELIVERY_TIME_ITEM_NOT_IN_STOCK = 7;
    public static final int DELIVERY_TIME_ITEM_IN_STOCK = 1;

    private final Item item;
    private final int amount;
    private LocalDate shippingDate;
    private final Price groupPrice;

    public ItemGroup(Item item, int amount) {
        this.item = item;
        this.amount = amount;
        setShippingDate(item, amount);
        this.groupPrice = Price.calculatePrice(item.getPrice(), amount);
    }

    private void setShippingDate(Item item, int amount) {
        shippingDate = LocalDate.now();
        if (amount > item.getAmount()) {
            this.shippingDate = shippingDate.plusDays(DELIVERY_TIME_ITEM_NOT_IN_STOCK);
        } else {
            this.shippingDate = shippingDate.plusDays(DELIVERY_TIME_ITEM_IN_STOCK);
        }
    }

    public Item getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    public LocalDate getShippingDate() {
        return shippingDate;
    }

    public Price getGroupPrice() {
        return groupPrice;
    }
}
