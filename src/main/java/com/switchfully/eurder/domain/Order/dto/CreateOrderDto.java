package com.switchfully.eurder.domain.Order.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateOrderDto {
    private List<ItemGroupDto> itemGroups;

    public CreateOrderDto() {
    }

    public CreateOrderDto(List<ItemGroupDto> itemGroups) {
        this.itemGroups = itemGroups;
    }

    public void addItemToGroup(ItemGroupDto itemGroupDto) {
        if (itemGroups == null) itemGroups = new ArrayList<>();
        itemGroups.add(itemGroupDto);
    }
}
