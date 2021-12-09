package com.switchfully.eurder.domain.Order.dto;

import com.switchfully.eurder.domain.item.Currency;
import com.switchfully.eurder.domain.item.Price;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderReportDto {
    private List<OrderDto> orderList;
    private Price totalListPrice;

    public OrderReportDto() {
        this.orderList = new ArrayList<>();
        this.totalListPrice = new Price(0, Currency.EUR);
    }

    public OrderReportDto(List<OrderDto> orderList, Price totalListPrice) {
        this.orderList = orderList;
        this.totalListPrice = totalListPrice;
    }

    public OrderReportDto addToOrderList(OrderDto orderDto) {
        this.orderList.add(orderDto);
        return this;
    }
}
