package com.switchfully.eurder.api;

import com.switchfully.eurder.api.mapper.ItemMapper;
import com.switchfully.eurder.domain.item.StockLevel;
import com.switchfully.eurder.domain.item.dto.CreateItemDto;
import com.switchfully.eurder.domain.item.Item;
import com.switchfully.eurder.domain.item.dto.ItemDto;
import com.switchfully.eurder.domain.item.dto.UpdateItemDto;
import com.switchfully.eurder.security.Feature;
import com.switchfully.eurder.security.UserValidator;
import com.switchfully.eurder.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/items")
public class ItemController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final ItemMapper itemMapper;
    private final ItemService itemService;
    private final UserValidator userValidator;

    public ItemController(ItemMapper itemMapper, ItemService itemService, UserValidator userValidator) {
        this.itemMapper = itemMapper;
        this.itemService = itemService;
        this.userValidator = userValidator;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestBody CreateItemDto createItemDto, @RequestHeader(required = false) String authorization) {
        logger.info("Method createItem called");
        userValidator.assertUserTypeForFeature(Feature.ADD_ITEM, authorization);
        Item newItem = itemMapper.mapCreateItemDtoToItem(createItemDto);
        Item savedItem = itemService.createItem(newItem);
        ItemDto itemDto = itemMapper.mapItemToDto(savedItem);
        logger.info("Method createItem executed successfully");
        return itemDto;
    }

    @PutMapping(path = "/{itemId}", produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto updateItem(@PathVariable("itemId") String itemId, @RequestBody UpdateItemDto updateItemDto, @RequestHeader(required = false) String authorization) {
        logger.info("Method updateItem called");
        userValidator.assertUserTypeForFeature(Feature.UPDATE_ITEM, authorization);
        Item item = itemService.getItemBy(itemId);
        Item updated = itemMapper.mapUpdateItemDtoToExistingItem(updateItemDto, item);
        updated = itemService.updateItem(updated);
        ItemDto itemDto = itemMapper.mapItemToDto(updated);
        logger.info("Method updateItem executed successfully");
        return itemDto;
    }

    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getItemOverview(@RequestParam(required = false) StockLevel stockLevel, @RequestHeader(required = false) String authorization) {
        logger.info("Method getItemOverview called");
        userValidator.assertUserTypeForFeature(Feature.ITEM_OVERVIEW, authorization);
        List<Item> savedItem = itemService.getItemsByUrgency();
        List<ItemDto> itemDtoList = savedItem.stream()
                .map(itemMapper::mapItemToDto)
                .filter(itemDto -> stockLevel == null || itemDto.getStockLevel().equals(stockLevel))
                .toList();
        logger.info("Method getItemOverview executed successfully");
        return itemDtoList;
    }
}
