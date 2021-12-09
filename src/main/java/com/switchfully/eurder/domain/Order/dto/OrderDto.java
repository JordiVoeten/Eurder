package com.switchfully.eurder.domain.Order.dto;

import com.switchfully.eurder.domain.item.Currency;
import com.switchfully.eurder.domain.item.Price;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderDto {
    private String id;
    private List<ItemGroupReportDto> itemGroupReportDto;
    private Price price;

    public OrderDto() {
    }

    public OrderDto(String id, List<ItemGroupReportDto> itemGroupReportDto, Price price) {
        this.id = id;
        this.itemGroupReportDto = itemGroupReportDto;
        this.price = price;
    }
}
