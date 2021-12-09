package com.switchfully.eurder.service;

import com.switchfully.eurder.api.mapper.ItemMapper;
import com.switchfully.eurder.domain.Order.ItemGroup;
import com.switchfully.eurder.domain.Order.Order;
import com.switchfully.eurder.domain.Order.dto.ItemGroupDto;
import com.switchfully.eurder.domain.exceptions.InvalidOrderException;
import com.switchfully.eurder.domain.exceptions.InvalidUserException;
import com.switchfully.eurder.domain.item.Currency;
import com.switchfully.eurder.domain.item.Item;
import com.switchfully.eurder.domain.item.Price;
import com.switchfully.eurder.domain.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
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
    @Autowired
    private ItemMapper itemMapper;

    Item item;
    User user;
    ItemGroup oneGroup;
    List<ItemGroup> itemGroups;

    @BeforeEach
    void setup() {
        item = new Item("Phone", "Used to call and text others", new Price(22, Currency.EUR), 5);
        itemService.createItem(item);
        user = new User("Jordi", "Voeten", "jordi@email.com", "Belgium", "01235");
        user = userService.createUser(user);
        oneGroup = new ItemGroup(item, 3);
        itemGroups = new ArrayList<>();
        itemGroups.add(oneGroup);
    }

    @Test
    void givenAValidOrder_whenOrderingItems_thenOrderIsCreated() {
        // Given
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
        Order order = new Order(itemGroups, user.getId());
        orderService.createItem(order);
        List<Order> validOrderList = List.of(order);
        // When
        List<Order> orderList = orderService.getOrders();

        // Then
        Assertions.assertThat(orderList).isEqualTo(validOrderList);
    }

    @Test
    void givenAValidOrderThatIsAdded_whenGettingOrdersByUser_thenGetList() {
        // Given
        Order order = new Order(itemGroups, user.getId());
        orderService.createItem(order);
        List<Order> validOrderList = List.of(order);
        // When
        List<Order> orderList = orderService.getOrdersByUser(user.getId());

        // Then
        Assertions.assertThat(orderList).isEqualTo(validOrderList);
    }


    @Test
    void givenAValidOrderWithTooLittleStock_whenOrderingItems_thenOrderIsCreated() {
        // Given
        Item item2 = new Item("Phone2", "Used to call and text others", new Price(22, Currency.EUR), 1);
        itemService.createItem(item2);
        oneGroup = new ItemGroup(item2, 3);
        itemGroups = new ArrayList<>();
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
        ItemGroup itemGroup = new ItemGroup(item, -3);
        List<ItemGroup> itemGroupList = new ArrayList<>();
        itemGroupList.add(itemGroup);

        Order order = new Order(itemGroupList, user.getId());

        // When
        // Then
        Assertions.assertThatThrownBy(() -> orderService.createItem(order))
                .isInstanceOf(InvalidOrderException.class)
                .hasMessage("The order can not have negative item amounts.");
    }


    @Test
    void givenAnOrderWithoutItems_whenOrderingItems_thenInvalidOrderException() {
        // Given
        List<ItemGroup> itemGroupList = new ArrayList<>();
        Order order = new Order(itemGroupList, user.getId());

        // When
        // Then
        Assertions.assertThatThrownBy(() -> orderService.createItem(order))
                .isInstanceOf(InvalidOrderException.class)
                .hasMessage("The order needs items.");
    }

    @Test
    void givenAnInvalidOrderWithoutRegisteredUser_whenOrderingItems_thenInvalidUserException() {
        // Given
        User newUser = new User("Jordi", "Voeten", "another@email.com", "Belgium", "01235");
        Order order = new Order(itemGroups, newUser.getId());
        // When
        // Then
        Assertions.assertThatThrownBy(() -> orderService.createItem(order))
                .isInstanceOf(InvalidUserException.class)
                .hasMessage("User with id: " + newUser.getId() + " does not exist");
    }

    @Test
    void givenAValidOrderThatIsAddedSetShippingDate_whenGettingOrdersShippedToday_thenGetList() {
        // Given
        oneGroup.setShippingDate(LocalDate.now());
        List<ItemGroup> itemGroupList = new ArrayList<>();
        itemGroupList.add(oneGroup);
        Order order = new Order(itemGroupList, user.getId());
        orderService.createItem(order);

        ItemGroupDto itemGroupDto = itemMapper.mapItemGroupToItemGroupDto(oneGroup);
        itemGroupDto.setAddress(user.getAddress());
        List<ItemGroupDto> validList = List.of(itemGroupDto);
        // When
        List<ItemGroupDto> orderList = orderService.getGroupsShippedToday();

        // Then
        Assertions.assertThat(orderList.size()).isEqualTo(validList.size());
    }

    @Test
    void givenAValidOrder_whenReorderingItems_thenNewOrderIsCreated() {
        // Given
        Order order = new Order(itemGroups, user.getId());

        // When
        orderService.createItem(order);
        Order saved = orderService.reorderOrder(order.getId(), user.getId());

        // Then
        Assertions.assertThat(itemGroups.size()).isEqualTo(saved.getItemGroups().size());
    }

    @Test
    void givenAnInvalidOrderId_whenReorderingItems_thenInvalidOrderException() {
        // Given
        Order order = new Order(itemGroups, user.getId());

        // When
        // Then
        Assertions.assertThatThrownBy(() -> orderService.reorderOrder(order.getId(), user.getId()))
                .isInstanceOf(InvalidOrderException.class)
                .hasMessage("The order does not exist or the user is not the same.");
    }

    @Test
    void givenAnInvalidUserId_whenReorderingItems_thenInvalidOrderException() {
        // Given
        User newUser = new User("Jordi", "Voeten", "another@email.com", "Belgium", "01235");

        Order order = new Order(itemGroups, user.getId());
        orderService.createItem(order);
        // When
        // Then
        Assertions.assertThatThrownBy(() -> orderService.reorderOrder(order.getId(), newUser.getId()))
                .isInstanceOf(InvalidOrderException.class)
                .hasMessage("The order does not exist or the user is not the same.");
    }
}