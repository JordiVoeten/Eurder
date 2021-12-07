package com.switchfully.eurder.repository;

import com.switchfully.eurder.domain.exceptions.InvalidItemException;
import com.switchfully.eurder.domain.item.Item;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ItemRepository {
    public List<Item> itemList;

    public ItemRepository() {
        this.itemList = new ArrayList<>();
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public Item addItem(Item item) {
        itemList.add(item);
        return item;
    }

    public Item updateItem(Item updatedItem) {
        if (itemList.stream().noneMatch(item -> item.getId().equals(updatedItem.getId()))) {
            throw new InvalidItemException("The item with id: " + updatedItem.getId() + " does not exist.");
        }
        itemList.stream()
                .filter(item -> item.getId().equals(updatedItem.getId()))
                .forEach(item -> item = updatedItem);

        return itemList.stream()
                .filter(item -> item.getId().equals(updatedItem.getId()))
                .findFirst()
                .orElse(null);
    }
}
