package com.switchfully.eurder.domain.Order.dto;


import com.switchfully.eurder.domain.item.Price;

import java.time.LocalDate;
import java.util.Objects;

public class ItemGroupDto {
    private String itemId;
    private int amount;
    private LocalDate shippingDate;
    private Price price;
    private String address;

    public ItemGroupDto setItemId(String itemId) {
        this.itemId = itemId;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public ItemGroupDto setAddress(String address) {
        this.address = address;
        return this;
    }

    public LocalDate getShippingDate() {
        return shippingDate;
    }

    public ItemGroupDto setShippingDate(LocalDate shippingDate) {
        this.shippingDate = shippingDate;
        return this;
    }

    public Price getPrice() {
        return price;
    }

    public ItemGroupDto setPrice(Price price) {
        this.price = price;
        return this;
    }

    public String getItemId() {
        return itemId;
    }

    public int getAmount() {
        return amount;
    }

    public ItemGroupDto setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemGroupDto that = (ItemGroupDto) o;
        return amount == that.amount && Objects.equals(itemId, that.itemId) && Objects.equals(shippingDate, that.shippingDate) && Objects.equals(price, that.price) && Objects.equals(address, that.address);
    }
}
