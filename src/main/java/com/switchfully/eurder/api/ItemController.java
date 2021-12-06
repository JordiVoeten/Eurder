package com.switchfully.eurder.api;

import com.switchfully.eurder.api.mapper.ItemMapper;
import com.switchfully.eurder.domain.item.dto.CreateItemDto;
import com.switchfully.eurder.domain.item.Item;
import com.switchfully.eurder.domain.item.dto.ItemDto;
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

    public ItemController(ItemMapper itemMapper, ItemService itemService) {
        this.itemMapper = itemMapper;
        this.itemService = itemService;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestBody CreateItemDto createItemDto){
        logger.info("Method createItem called");
        Item newItem = itemMapper.mapCreateItemDtoToItem(createItemDto);
        Item savedItem = itemService.createItem(newItem);
        ItemDto itemDto = itemMapper.mapItemToDto(savedItem);
        logger.info("Method createItem executed successfully");
        return itemDto;
    }

    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getItems(){
        logger.info("Method getItems called");
        List<Item> savedItem = itemService.getItems();
        List<ItemDto> itemDtoList = savedItem.stream()
                .map(itemMapper::mapItemToDto)
                .toList();
        logger.info("Method getItems executed successfully");
        return itemDtoList;
    }
}
