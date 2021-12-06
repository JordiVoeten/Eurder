package com.switchfully.eurder.domain.Order.dto;

import com.switchfully.eurder.domain.item.Price;

import java.util.List;

public class OrderDto {
    private String id;
    private List<ItemGroupReportDto> itemGroups;
    private String customerId;
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

    public String getCustomerId() {
        return customerId;
    }

    public OrderDto setCustomerId(String customerId) {
        this.customerId = customerId;
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
