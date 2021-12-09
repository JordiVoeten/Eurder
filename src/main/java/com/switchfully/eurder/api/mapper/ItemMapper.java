package com.switchfully.eurder.api.mapper;

import com.switchfully.eurder.domain.Order.ItemGroup;
import com.switchfully.eurder.domain.Order.dto.ItemGroupDto;
import com.switchfully.eurder.domain.item.StockLevel;
import com.switchfully.eurder.domain.item.dto.CreateItemDto;
import com.switchfully.eurder.domain.item.Item;
import com.switchfully.eurder.domain.item.dto.ItemDto;
import com.switchfully.eurder.domain.item.dto.UpdateItemDto;
import com.switchfully.eurder.service.ItemService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemMapper {
    private final ItemService itemService;

    public ItemMapper(ItemService itemService) {
        this.itemService = itemService;
    }

    public Item mapCreateItemDtoToItem(CreateItemDto createItemDto) {
        return new Item(createItemDto.getName(), createItemDto.getDescription(), createItemDto.getPrice(), createItemDto.getAmount());
    }

    public ItemDto mapItemToDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .stockLevel(StockLevel.getStockLevelForAmount(item.getAmount()))
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice())
                .amount(item.getAmount())
                .build();
    }

    public List<ItemGroup> mapItemGroupListDtoToItemGroupList(List<ItemGroupDto> itemGroups) {
        return itemGroups.stream().map(this::mapItemGroupDtoToItemGroup).toList();
    }

    public ItemGroup mapItemGroupDtoToItemGroup(ItemGroupDto itemGroupDto) {
        Item item = itemService.getItemBy(itemGroupDto.getItemId());
        return new ItemGroup(item, itemGroupDto.getAmount());
    }

    public ItemGroupDto mapItemGroupToItemGroupDto(ItemGroup itemGroup) {
        return ItemGroupDto.builder()
                .itemId(itemGroup.getItem().getId())
                .amount(itemGroup.getAmount())
                .shippingDate(itemGroup.getShippingDate())
                .price(itemGroup.getGroupPrice())
                .build();
    }

    public Item mapUpdateItemDtoToExistingItem(UpdateItemDto updateItemDto, Item item) {
        if (updateItemDto.getName() != null) item.setName(updateItemDto.getName());
        if (updateItemDto.getDescription() != null) item.setDescription(updateItemDto.getDescription());
        if (updateItemDto.getAmount() > 0) item.setAmount(updateItemDto.getAmount());
        if (updateItemDto.getPrice() != null) item.setPrice(updateItemDto.getPrice());
        return item;
    }
}
