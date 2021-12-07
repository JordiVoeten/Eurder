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

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(LocalDate shippingDate) {
        this.shippingDate = shippingDate;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public String getItemId() {
        return itemId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemGroupDto that = (ItemGroupDto) o;
        return amount == that.amount && Objects.equals(itemId, that.itemId) && Objects.equals(shippingDate, that.shippingDate) && Objects.equals(price, that.price) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, amount, shippingDate, price, address);
    }
}
