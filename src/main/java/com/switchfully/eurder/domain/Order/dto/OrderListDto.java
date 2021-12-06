package com.switchfully.eurder.domain.Order.dto;

import com.switchfully.eurder.domain.item.Currency;
import com.switchfully.eurder.domain.item.Price;

import java.util.ArrayList;
import java.util.List;

public class OrderListDto {
    private List<OrderDto> orderList;
    private Price totalListPrice;

    public OrderListDto() {
        this.orderList = new ArrayList<>();
        this.totalListPrice = new Price(0, Currency.EUR);
    }

    public List<OrderDto> getOrderList() {
        return orderList;
    }

    public OrderListDto setOrderList(List<OrderDto> orderList) {
        this.orderList = orderList;
        return this;
    }

    public OrderListDto addToOrderList(OrderDto orderDto) {
        this.orderList.add(orderDto);
        return this;
    }

    public Price getTotalListPrice() {
        return totalListPrice;
    }

    public OrderListDto setTotalListPrice(Price totalListPrice) {
        this.totalListPrice = totalListPrice;
        return this;
    }
}
