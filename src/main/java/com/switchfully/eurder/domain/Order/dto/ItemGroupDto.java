package com.switchfully.eurder.domain.Order.dto;


import com.switchfully.eurder.domain.item.Price;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ItemGroupDto {
    private String itemId;
    private int amount;
    private LocalDate shippingDate;
    private Price price;
    private String address;

    public ItemGroupDto() {
    }

    public ItemGroupDto(String itemId, int amount, LocalDate shippingDate, Price price, String address) {
        this.itemId = itemId;
        this.amount = amount;
        this.shippingDate = shippingDate;
        this.price = price;
        this.address = address;
    }
}
