package com.switchfully.eurder.domain.Order.dto;

import com.switchfully.eurder.domain.item.Currency;
import com.switchfully.eurder.domain.item.Price;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderDto {
    private String id;
    private List<ItemGroupReportDto> itemGroupReportDto;
    private BigDecimal value;
    private Currency currency;

    public String getId() {
        return id;
    }

    public OrderDto setId(String id) {
        this.id = id;
        return this;
    }

    public List<ItemGroupReportDto> getItemGroupReportDto() {
        return itemGroupReportDto;
    }

    public OrderDto setItemGroupReportDto(List<ItemGroupReportDto> itemGroupReportDto) {
        if (itemGroupReportDto == null) itemGroupReportDto = new ArrayList<>();
        this.itemGroupReportDto = itemGroupReportDto;
        return this;
    }

//    public Price getTotalPrice() {
//        return new Price(value.doubleValue(), currency);
//    }

    public BigDecimal getValue() {
        return value;
    }

    public OrderDto setValue(BigDecimal value) {
        this.value = value;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public OrderDto setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }
}
