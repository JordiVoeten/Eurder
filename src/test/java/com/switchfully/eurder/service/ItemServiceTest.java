package com.switchfully.eurder.service;

import com.switchfully.eurder.domain.exceptions.InvalidItemException;
import com.switchfully.eurder.domain.item.Currency;
import com.switchfully.eurder.domain.item.Item;
import com.switchfully.eurder.domain.item.Price;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Test
    void givenAValidItem_whenAddingThatItemToRepository_thenItemIsAdded() {
        // Given
        Item item = new Item("Phone", "Used to call and text others", new Price(22, Currency.EUR), 5);

        // When
        Item created = itemService.createItem(item);

        // Then
        Assertions.assertThat(item).isEqualTo(created);
    }
    @Test
    void givenAValidItem_whenSettingPriceAndAddingThatItemToRepository_thenItemIsAdded() {
        // Given
        Item item = new Item("Phone", "Used to call and text others", new Price(22, Currency.EUR), 5);

        // When
        item.getPrice().setValue(12);
        Item created = itemService.createItem(item);

        // Then
        Assertions.assertThat(item).isEqualTo(created);
    }

    @Test
    void givenAnItemWithInvalidName_whenAddingThatItemToRepository_thenThrowsInvalidItemException() {
        // Given
        Item item = new Item(null, "Used to call and text others", new Price(22, Currency.EUR), 5);

        // When
        // Then
        Assertions.assertThatThrownBy(() -> itemService.createItem(item))
                .isInstanceOf(InvalidItemException.class)
                .hasMessage("The name of the item is required.");
    }

    @Test
    void givenAnItemWithInvalidDescription_whenAddingThatItemToRepository_thenThrowsInvalidItemException() {
        // Given
        Item item = new Item("Phone", null, new Price(22, Currency.EUR), 5);

        // When
        // Then
        Assertions.assertThatThrownBy(() -> itemService.createItem(item))
                .isInstanceOf(InvalidItemException.class)
                .hasMessage("The description of the item is required.");
    }

    @Test
    void givenAnItemWithInvalidPrice_whenAddingThatItemToRepository_thenThrowsInvalidItemException() {
        // Given
        Item item = new Item("Phone", "Used to call and text others", null, 5);

        // When
        // Then
        Assertions.assertThatThrownBy(() -> itemService.createItem(item))
                .isInstanceOf(InvalidItemException.class)
                .hasMessage("The price of the item is required.");
    }

    @Test
    void givenAnItemWithANegativePrice_whenAddingThatItemToRepository_thenThrowsInvalidItemException() {
        // Given
        Item item = new Item("Phone", "Used to call and text others", new Price(-1, Currency.EUR), 5);

        // When
        // Then
        Assertions.assertThatThrownBy(() -> itemService.createItem(item))
                .isInstanceOf(InvalidItemException.class)
                .hasMessage("The price of the item has to be larger than 0.");
    }

    @Test
    void givenAnItemWithANegativeAmount_whenAddingThatItemToRepository_thenThrowsInvalidItemException() {
        // Given
        Item item = new Item("Phone", "Used to call and text others", new Price(21, Currency.EUR), -5);

        // When
        // Then
        Assertions.assertThatThrownBy(() -> itemService.createItem(item))
                .isInstanceOf(InvalidItemException.class)
                .hasMessage("The amount of the item has to be larger than 0.");
    }

    @Test
    void givenThreeItemsAddedToList_whenGetAllItems_thenListShouldMatch() {
        // Given
        Item item = new Item("Phone", "Used to call and text others", new Price(400, Currency.EUR), 5);
        Item item2 = new Item("Bike", "Used to drive around", new Price(200, Currency.EUR), 2);
        Item item3 = new Item("Pen", "Used to write something down", new Price(2, Currency.EUR), 10);
        itemService.createItem(item);
        itemService.createItem(item2);
        itemService.createItem(item3);
        List<Item> validItemList = List.of(item, item2, item3);
        // When
        List<Item> itemList = itemService.getItems();

        // Then
        Assertions.assertThat(itemList).isEqualTo(validItemList);
    }

    @Test
    void givenThreeItemsAddedToList_whenGetItemById_thenGetThatItem() {
        // Given
        Item item = new Item("Phone", "Used to call and text others", new Price(400, Currency.EUR), 5);
        Item item2 = new Item("Bike", "Used to drive around", new Price(200, Currency.EUR), 2);
        Item item3 = new Item("Pen", "Used to write something down", new Price(2, Currency.EUR), 10);
        itemService.createItem(item);
        itemService.createItem(item2);
        itemService.createItem(item3);
        // When
        Item  found = itemService.getItemBy(item.getId());

        // Then
        Assertions.assertThat(found).isEqualTo(item);
    }
}