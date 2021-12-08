package com.switchfully.eurder.domain.Order.dto;

import java.util.ArrayList;
import java.util.List;

public class CreateOrderDto {
    private List<ItemGroupDto> itemGroups;

    public List<ItemGroupDto> getItemGroups() {
        return itemGroups;
    }

    public void addItemToGroup(ItemGroupDto itemGroupDto) {
        if (itemGroups == null) itemGroups = new ArrayList<>();
        itemGroups.add(itemGroupDto);
    }
}
