package com.switchfully.eurder.service;

import com.switchfully.eurder.domain.exceptions.InvalidItemException;
import com.switchfully.eurder.domain.item.Currency;
import com.switchfully.eurder.domain.item.Item;
import com.switchfully.eurder.domain.item.Price;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    Item item;
    Item item2;
    Item item3;

    @BeforeEach
    void setup() {
        item = new Item("Phone", "Used to call and text others", new Price(22, Currency.EUR), 5);
        item2 = new Item("Bike", "Used to call and text others", new Price(22, Currency.EUR), 1);
        item3 = new Item("Car", "Used to call and text others", new Price(22, Currency.EUR), 10);
        itemService.createItem(item2);
        itemService.createItem(item3);
    }

    @Test
    void givenAValidItem_whenAddingThatItemToRepository_thenItemIsAdded() {
        // Given
        // When
        Item created = itemService.createItem(item);

        // Then
        Assertions.assertThat(item).isEqualTo(created);
    }

    @Test
    void givenAValidItem_whenSettingPriceAndAddingThatItemToRepository_thenItemIsAdded() {
        // Given
        // When
        item.getPrice().setValue(12);
        Item created = itemService.createItem(item);
        // Then
        Assertions.assertThat(item).isEqualTo(created);
    }

    @Test
    void givenAValidItemList_whenGetItemsByUrgency_thenItemListIsSorted() {
        // Given
        itemService.createItem(item);
        List<Item> validSortedList = List.of(item2, item, item3);
        // When
        List<Item> urgencyList = itemService.getItemsByUrgency();
        // Then
        Assertions.assertThat(urgencyList).isEqualTo(validSortedList);
    }

    @Test
    void givenAnItemWithInvalidName_whenAddingThatItemToRepository_thenThrowsInvalidItemException() {
        // Given
        item.setName(null);
        // When
        // Then
        Assertions.assertThatThrownBy(() -> itemService.createItem(item))
                .isInstanceOf(InvalidItemException.class)
                .hasMessage("The name of the item is required.");
    }

    @Test
    void givenAnItemWithAnExistingName_whenAddingThatItemToRepository_thenThrowsInvalidItemException() {
        // Given
        Item itemSameName = new Item("Phone", "Used to call and text others", new Price(22, Currency.EUR), 5);
        itemService.createItem(item);
        // When
        // Then
        Assertions.assertThatThrownBy(() -> itemService.createItem(itemSameName))
                .isInstanceOf(InvalidItemException.class)
                .hasMessage("The item with name: Phone already exists.");
    }

    @Test
    void givenAnItemWithInvalidDescription_whenAddingThatItemToRepository_thenThrowsInvalidItemException() {
        // Given
        item.setDescription(null);
        // When
        // Then
        Assertions.assertThatThrownBy(() -> itemService.createItem(item))
                .isInstanceOf(InvalidItemException.class)
                .hasMessage("The description of the item is required.");
    }

    @Test
    void givenAnItemWithInvalidPrice_whenAddingThatItemToRepository_thenThrowsInvalidItemException() {
        // Given
        item.setPrice(null);
        // When
        // Then
        Assertions.assertThatThrownBy(() -> itemService.createItem(item))
                .isInstanceOf(InvalidItemException.class)
                .hasMessage("The price of the item is required.");
    }

    @Test
    void givenAnItemWithANegativePrice_whenAddingThatItemToRepository_thenThrowsInvalidItemException() {
        // Given
        item.setPrice(new Price(-1, Currency.EUR));
        // When
        // Then
        Assertions.assertThatThrownBy(() -> itemService.createItem(item))
                .isInstanceOf(InvalidItemException.class)
                .hasMessage("The price of the item has to be larger than 0.");
    }

    @Test
    void givenAnItemWithANegativeAmount_whenAddingThatItemToRepository_thenThrowsInvalidItemException() {
        // Given
        item.setAmount(-5);
        // When
        // Then
        Assertions.assertThatThrownBy(() -> itemService.createItem(item))
                .isInstanceOf(InvalidItemException.class)
                .hasMessage("The amount of the item has to be larger than 0.");
    }

    @Test
    void givenThreeItemsAddedToList_whenGetAllItems_thenListShouldMatch() {
        // Given
        itemService.createItem(item);
        List<Item> validItemList = List.of(item, item2, item3);
        // When
        List<Item> itemList = itemService.getItems();

        // Then
        Assertions.assertThat(itemList).containsAll(validItemList);
    }

    @Test
    void givenThreeItemsAddedToList_whenGetItemById_thenGetThatItem() {
        // Given
        itemService.createItem(item);

        // When
        Item found = itemService.getItemBy(item.getId());

        // Then
        Assertions.assertThat(found).isEqualTo(item);
    }

    @Test
    void givenThreeItemsAddedToList_whenUpdateItemById_thenGetThatItem() {
        // Given
        itemService.createItem(item);
        item.setName("newPhone");
        item.setDescription("newDescription");
        item.setPrice(new Price(399, Currency.EUR));
        item.setAmount(23);

        // When
        Item found = itemService.updateItem(item);

        // Then
        Assertions.assertThat(found.getName()).isEqualTo(item.getName());
        Assertions.assertThat(found.getDescription()).isEqualTo(item.getDescription());
        Assertions.assertThat(found.getAmount()).isEqualTo(item.getAmount());
        Assertions.assertThat(found.getPrice()).isEqualTo(item.getPrice());
    }

    @Test
    void givenEmptyList_whenUpdateItemByIdThatDoesNotExist_thenInvalidItemException() {
        // Given
        // When
        // Then
        Assertions.assertThatThrownBy(() -> itemService.updateItem(item))
                .isInstanceOf(InvalidItemException.class)
                .hasMessage("The item with id: " + item.getId() + " does not exist.");
    }

    @Test
    void givenAValidItem_whenChangingAmount_thenItemIsChanged() {
        // Given
        Item created = itemService.createItem(item);
        // When
        itemService.removeAmountFromStock(created, 2);
        Item found = itemService.getItemBy(created.getId());
        // Then
        Assertions.assertThat(found.getAmount()).isEqualTo(3);
    }
}