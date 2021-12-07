package com.switchfully.eurder.domain.Order.dto;

import com.switchfully.eurder.domain.item.Price;

import java.util.List;

public class OrderDto {
    private String id;
    private List<ItemGroupReportDto> itemGroups;
    private Price totalPrice;

    public String getId() {
        return id;
    }

    public OrderDto setId(String id) {
        this.id = id;
        return this;
    }

    public List<ItemGroupReportDto> getItemGroups() {
        return itemGroups;
    }

    public OrderDto setItemGroups(List<ItemGroupReportDto> itemGroups) {
        this.itemGroups = itemGroups;
        return this;
    }

    public Price getTotalPrice() {
        return totalPrice;
    }

    public OrderDto setTotalPrice(Price totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }
}
