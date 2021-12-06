package com.switchfully.eurder.api.mapper;

import com.switchfully.eurder.domain.Order.CreateOrderDto;
import com.switchfully.eurder.domain.Order.Order;
import com.switchfully.eurder.domain.Order.OrderDto;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class OrderMapper {
    private ItemMapper itemMapper;

    public OrderMapper(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    public Order mapCreateOrderDtoToOrder(CreateOrderDto createOrderDto) {
        return new Order(itemMapper.mapItemGroupListDtoToItemGroupList(Arrays.stream(createOrderDto.getItemGroups()).toList())
                , createOrderDto.getCustomerId());
    }

    public OrderDto mapOrderToDto(Order order) {
        return new OrderDto()
                .setId(order.getId())
                .setItemGroups(order.getItemGroups())
                .setCustomerId(order.getCustomerId())
                .setTotalPrice(order.getTotalPrice());
    }
}
