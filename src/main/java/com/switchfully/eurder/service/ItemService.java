package com.switchfully.eurder.service;

import com.switchfully.eurder.domain.exceptions.InvalidItemException;
import com.switchfully.eurder.domain.item.Item;
import com.switchfully.eurder.domain.item.Price;
import com.switchfully.eurder.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item createItem(Item newItem) {
        validateItem(newItem);
        Item item = itemRepository.addItem(newItem);
        return item;
    }

    public List<Item> getItems() {
        return itemRepository.getItemList();
    }

    private void validateItem(Item newItem) {
        assertNullOrEmpty(newItem.getName(), "name");
        assertNullOrEmpty(newItem.getDescription(), "description");
        assertValidPrice(newItem.getPrice());
        assertPositiveAmount(newItem.getAmount());
    }

    private void assertPositiveAmount(int amount) {
        if (amount < 0) {
            throw new InvalidItemException("The amount of the item has to be larger than 0.");
        }
    }

    private void assertValidPrice(Price price) {
        if (price == null) {
            throw new InvalidItemException("The price of the item is required.");
        } else if (price.getValue().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidItemException("The price of the item has to be larger than 0.");
        }
    }

    private void assertNullOrEmpty(String value, String fieldName) {
        if (value == null || value.trim().equals("")) {
            throw new InvalidItemException("The " + fieldName + " of the item is required.");
        }
    }
}
