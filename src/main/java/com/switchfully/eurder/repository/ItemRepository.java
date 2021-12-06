package com.switchfully.eurder.repository;

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
}
