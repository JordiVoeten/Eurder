package com.switchfully.eurder.domain.Order.dto;

import com.switchfully.eurder.domain.item.Currency;
import com.switchfully.eurder.domain.item.Price;

import java.util.ArrayList;
import java.util.List;

public class OrderReportDto {
    private List<OrderDto> orderList;
    private Price totalListPrice;

    public OrderReportDto() {
        this.orderList = new ArrayList<>();
        this.totalListPrice = new Price(0, Currency.EUR);
    }

    public List<OrderDto> getOrderList() {
        return orderList;
    }

    public OrderReportDto setOrderList(List<OrderDto> orderList) {
        this.orderList = orderList;
        return this;
    }

    public OrderReportDto addToOrderList(OrderDto orderDto) {
        this.orderList.add(orderDto);
        return this;
    }

    public Price getTotalListPrice() {
        return totalListPrice;
    }

    public OrderReportDto setTotalListPrice(Price totalListPrice) {
        this.totalListPrice = totalListPrice;
        return this;
    }
}
