package com.switchfully.eurder.service;

import com.switchfully.eurder.domain.exceptions.InvalidItemException;
import com.switchfully.eurder.domain.item.Item;
import com.switchfully.eurder.domain.item.Price;
import com.switchfully.eurder.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item createItem(Item newItem) {
        validateItem(newItem);
        return itemRepository.addItem(newItem);
    }

    public List<Item> getItems() {
        return itemRepository.getItemList();
    }

    public List<Item> getItemsByUrgency() {
        return itemRepository.getItemList().stream().sorted(Comparator.comparingInt(Item::getAmount)).toList();
    }

    public Item getItemBy(String id) {
        return itemRepository.getItemList().stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new InvalidItemException("Item with id: " + id + " does not exist."));
    }

    private void validateItem(Item item) {
        assertItemNameNotInUse(item.getName());
        assertNullOrEmpty(item.getName(), "name");
        assertNullOrEmpty(item.getDescription(), "description");
        assertValidPrice(item.getPrice());
        assertPositiveAmount(item.getAmount());
    }

    private void assertItemNameNotInUse(String name) {
        if (itemRepository.getItemList().stream().anyMatch(item -> item.getName().equals(name))) {
            throw new InvalidItemException("The item with name: " + name + " already exists.");
        }
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

    public Item updateItem(Item updatedItem) {
        boolean nameTakenNotThisItem = itemRepository.getItemList().stream()
                .filter(item -> item.getName().equals(updatedItem.getName()))
                .anyMatch(item -> !item.getId().equals(updatedItem.getId()));
        if (nameTakenNotThisItem) {
            throw new InvalidItemException("The item with name: " + updatedItem.getName() + " already exists.");
        }
        return itemRepository.updateItem(updatedItem);
    }

    public void removeAmount(Item item, int amount) {
        item.setAmount(item.getAmount() - amount);
        updateItem(item);
    }
}
