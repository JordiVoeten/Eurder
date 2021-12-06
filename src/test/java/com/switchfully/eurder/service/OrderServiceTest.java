package com.switchfully.eurder.service;

import com.switchfully.eurder.domain.Order.ItemGroup;
import com.switchfully.eurder.domain.Order.Order;
import com.switchfully.eurder.domain.exceptions.InvalidOrderException;
import com.switchfully.eurder.domain.exceptions.InvalidUserException;
import com.switchfully.eurder.domain.item.Currency;
import com.switchfully.eurder.domain.item.Item;
import com.switchfully.eurder.domain.item.Price;
import com.switchfully.eurder.domain.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OrderServiceTest {
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;


    @Test
    void givenAValidOrder_whenOrderingItems_thenOrderIsCreated() {
        // Given
        Item item = new Item("Phone", "Used to call and text others", new Price(22, Currency.EUR), 5);
        itemService.createItem(item);
        User user = new User("Jordi", "Voeten", "jordi@email.com", "Belgium", "01235");
        user = userService.createUser(user);
        ItemGroup oneGroup = new ItemGroup(item, 3);
        List<ItemGroup> itemGroups = new ArrayList<>();
        itemGroups.add(oneGroup);
        Order order = new Order(itemGroups, user.getId());
        BigDecimal validTotalValue = new BigDecimal(3 * 22).setScale(2, RoundingMode.HALF_EVEN);

        // When
        Order saved = orderService.createItem(order);

        // Then

        Assertions.assertThat(order).isEqualTo(saved);
        Assertions.assertThat(order.getId()).isEqualTo(saved.getId());
        Assertions.assertThat(order.getItemGroups()).isEqualTo(itemGroups);
        Assertions.assertThat(order.getTotalPrice().getValue()).isEqualTo(validTotalValue);
    }

    @Test
    void givenAValidOrderThatIsAdded_whenGettingOrders_thenGetList() {
        // Given
        Item item = new Item("Phone", "Used to call and text others", new Price(22, Currency.EUR), 5);
        itemService.createItem(item);
        User user = new User("Jordi", "Voeten", "jordi@email.com", "Belgium", "01235");
        user = userService.createUser(user);
        ItemGroup oneGroup = new ItemGroup(item, 3);
        List<ItemGroup> itemGroups = new ArrayList<>();
        itemGroups.add(oneGroup);
        Order order = new Order(itemGroups, user.getId());
        Order saved = orderService.createItem(order);
        List<Order> validOrderList = List.of(order);
        // When
        List<Order> orderList = orderService.getOrders();

        // Then
        Assertions.assertThat(orderList).isEqualTo(validOrderList);
    }


    @Test
    void givenAValidOrderWithTooLittleStock_whenOrderingItems_thenOrderIsCreated() {
        // Given
        Item item = new Item("Phone", "Used to call and text others", new Price(22, Currency.EUR), 2);
        itemService.createItem(item);
        User user = new User("Jordi", "Voeten", "jordi@email.com", "Belgium", "01235");
        user = userService.createUser(user);
        ItemGroup oneGroup = new ItemGroup(item, 3);
        List<ItemGroup> itemGroups = new ArrayList<>();
        itemGroups.add(oneGroup);
        Order order = new Order(itemGroups, user.getId());
        Price validTotalValue = oneGroup.getGroupPrice();

        // When
        Order saved = orderService.createItem(order);

        // Then
        Assertions.assertThat(order).isEqualTo(saved);
        Assertions.assertThat(order.getItemGroups()).isEqualTo(itemGroups);
        Assertions.assertThat(order.getItemGroups().stream().map(ItemGroup::getShippingDate).toList())
                .containsAll(itemGroups.stream().map(ItemGroup::getShippingDate).toList());
        Assertions.assertThat(order.getTotalPrice().getValue()).isEqualTo(validTotalValue.getValue());
    }

    @Test
    void givenAnOrderWithNegativeItemGroupAmount_whenOrderingItems_thenInvalidOrderException() {
        // Given
        Item item = new Item("Phone", "Used to call and text others", new Price(22, Currency.EUR), 5);
        itemService.createItem(item);
        User user = new User("Jordi", "Voeten", "jordi@email.com", "Belgium", "01235");
        user = userService.createUser(user);
        ItemGroup oneGroup = new ItemGroup(item, -3);
        List<ItemGroup> itemGroups = new ArrayList<>();
        itemGroups.add(oneGroup);
        Order order = new Order(itemGroups, user.getId());
        BigDecimal validTotalValue = new BigDecimal(oneGroup.getAmount() * 22).setScale(2, RoundingMode.HALF_EVEN);

        // When
        // Then
        Assertions.assertThatThrownBy(() -> orderService.createItem(order))
                .isInstanceOf(InvalidOrderException.class)
                .hasMessage("The order can not have negative item amounts.");
    }


    @Test
    void givenAnOrderWithoutItems_whenOrderingItems_thenInvalidOrderException() {
        // Given
        User user = new User("Jordi", "Voeten", "jordi@email.com", "Belgium", "01235");
        user = userService.createUser(user);
        List<ItemGroup> itemGroups = new ArrayList<>();
        Order order = new Order(itemGroups, user.getId());

        // When

        // Then
        Assertions.assertThatThrownBy(() -> orderService.createItem(order))
                .isInstanceOf(InvalidOrderException.class)
                .hasMessage("The order needs items.");
    }

    @Test
    void givenAnInvalidOrderWithoutRegisterdUser_whenOrderingItems_thenInvalidUserException() {
        // Given
        Item item = new Item("Phone", "Used to call and text others", new Price(22, Currency.EUR), 5);
        itemService.createItem(item);
        User user = new User("Jordi", "Voeten", "jordi@email.com", "Belgium", "01235");
        ItemGroup oneGroup = new ItemGroup(item, 3);
        List<ItemGroup> itemGroups = new ArrayList<>();
        itemGroups.add(oneGroup);
        Order order = new Order(itemGroups, user.getId());

        // When
        // Then
        Assertions.assertThatThrownBy(() -> orderService.createItem(order))
                .isInstanceOf(InvalidUserException.class)
                .hasMessage("User with id: " + user.getId() + " does not exist");
    }
}