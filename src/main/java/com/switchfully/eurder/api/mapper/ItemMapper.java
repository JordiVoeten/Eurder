package com.switchfully.eurder.api.mapper;

import com.switchfully.eurder.domain.item.CreateItemDto;
import com.switchfully.eurder.domain.item.Item;
import com.switchfully.eurder.domain.item.ItemDto;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {
    public Item mapCreateItemDtoToItem(CreateItemDto createItemDto) {
        return new Item(createItemDto.getName(), createItemDto.getDescription(), createItemDto.getPrice(), createItemDto.getAmount());
    }

    public ItemDto mapItemToDto(Item item) {
        return new ItemDto()
                .setId(item.getId())
                .setName(item.getName())
                .setDescription(item.getDescription())
                .setPrice(item.getPrice())
                .setAmount(item.getAmount());
    }
}
