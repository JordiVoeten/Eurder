package com.switchfully.eurder.domain.Order;

public class CreateOrderDto {
    private ItemGroupDto[] itemGroups;
    private String customerId;

    public ItemGroupDto[] getItemGroups() {
        return itemGroups;
    }

    public String getCustomerId() {
        return customerId;
    }

}
