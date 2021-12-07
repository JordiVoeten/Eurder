package com.switchfully.eurder.service;

import com.switchfully.eurder.api.mapper.ItemMapper;
import com.switchfully.eurder.domain.Order.ItemGroup;
import com.switchfully.eurder.domain.Order.Order;
import com.switchfully.eurder.domain.Order.dto.ItemGroupDto;
import com.switchfully.eurder.domain.exceptions.InvalidOrderException;
import com.switchfully.eurder.domain.item.Currency;
import com.switchfully.eurder.domain.item.Item;
import com.switchfully.eurder.domain.item.Price;
import com.switchfully.eurder.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemService itemService;
    private final UserService userService;
    private final ItemMapper itemMapper;

    public OrderService(OrderRepository orderRepository, ItemService itemService, UserService userService, ItemMapper itemMapper) {
        this.orderRepository = orderRepository;
        this.itemService = itemService;
        this.userService = userService;
        this.itemMapper = itemMapper;
    }

    public Order createItem(Order order) {
        assertValidItemGroups(order.getItemGroups());
        userService.getUserBy(order.getCustomerId());
        Price totalOrderPrice = calculateTotalOrderPrice(order);
        order.setTotalPrice(totalOrderPrice);
        return orderRepository.addOrder(order);
    }

    private void assertValidItemGroups(List<ItemGroup> itemGroups) {
        if (itemGroups == null || itemGroups.size() == 0) {
            throw new InvalidOrderException("The order needs items.");
        }
        if (itemGroups.stream().anyMatch(itemGroup -> itemGroup.getAmount() < 0)) {
            throw new InvalidOrderException("The order can not have negative item amounts.");
        }

    }

    private Price calculateTotalOrderPrice(Order order) {
        BigDecimal price = new BigDecimal(0);
        Currency currency = null;
        for (ItemGroup itemGroup : order.getItemGroups()) {
            Item item = itemService.getItemBy(itemGroup.getItem().getId());
            BigDecimal amount = new BigDecimal(itemGroup.getAmount());
            price = price.add(item.getPrice().getValue().multiply(amount));
            if (currency == null) currency = item.getPrice().getCurrency();
        }
        return new Price(price.doubleValue(), currency);
    }

    public List<Order> getOrders() {
        return orderRepository.getOrderList();
    }

    public List<Order> getOrdersByUser(String customerId) {
        return orderRepository.getOrderList().stream().filter(order -> order.getCustomerId().equals(customerId)).toList();
    }

    public List<ItemGroupDto> getGroupsShippedToday() {
        List<ItemGroupDto> itemGroupDtoList = new ArrayList<>();
        for (Order order : orderRepository.getOrderList()) {
            for (ItemGroup itemGroup : order.getItemGroups()) {
                if (itemGroup.getShippingDate().isEqual(LocalDate.now())) {
                    ItemGroupDto itemGroupDto = itemMapper.mapItemGroupToItemGroupDto(itemGroup);
                    itemGroupDto.setAddress(userService.getUserBy(order.getCustomerId()).getAddress());
                    itemGroupDtoList.add(itemGroupDto);
                }
            }
        }
        return itemGroupDtoList;
    }
}
