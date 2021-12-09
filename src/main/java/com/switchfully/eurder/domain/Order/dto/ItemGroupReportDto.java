package com.switchfully.eurder.domain.Order.dto;

 import com.switchfully.eurder.domain.item.Price;
 import lombok.Builder;
 import lombok.Getter;
 import lombok.Setter;

@Getter
@Setter
@Builder
public class ItemGroupReportDto {
    private String itemName;
    private int amount;
    private Price price;

    public ItemGroupReportDto() {
    }

    public ItemGroupReportDto(String itemName, int amount, Price price) {
        this.itemName = itemName;
        this.amount = amount;
        this.price = price;
    }
}
