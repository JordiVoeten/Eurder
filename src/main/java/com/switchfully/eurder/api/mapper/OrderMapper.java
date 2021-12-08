package com.switchfully.eurder.api.mapper;

import com.switchfully.eurder.domain.Order.*;
import com.switchfully.eurder.domain.Order.dto.CreateOrderDto;
import com.switchfully.eurder.domain.Order.dto.ItemGroupReportDto;
import com.switchfully.eurder.domain.Order.dto.OrderDto;
import com.switchfully.eurder.domain.Order.dto.OrderReportDto;
import com.switchfully.eurder.domain.item.Item;
import com.switchfully.eurder.domain.item.Price;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper {
    private final ItemMapper itemMapper;

    public OrderMapper(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    public Order mapCreateOrderDtoToOrder(CreateOrderDto createOrderDto, String customerId) {
        return new Order(itemMapper.mapItemGroupListDtoToItemGroupList(createOrderDto.getItemGroups())
                , customerId);
    }

    public OrderDto mapOrderToDto(Order order) {
        return new OrderDto()
                .setId(order.getId())
                .setItemGroupReportDto(mapItemGroupListToItemGroupReportDtoList(order))
                .setValue(order.getTotalPrice().getValue())
                .setCurrency(order.getTotalPrice().getCurrency());
    }

    private List<ItemGroupReportDto> mapItemGroupListToItemGroupReportDtoList(Order order) {
        return order.getItemGroups().stream()
                .map(this::mapItemGroupToItemGroupReportDto)
                .toList();
    }

    private ItemGroupReportDto mapItemGroupToItemGroupReportDto(ItemGroup itemGroup) {
        return new ItemGroupReportDto()
                .setItemName(itemGroup.getItem().getName())
                .setAmount(itemGroup.getAmount())
                .setItemGroupPrice(itemGroup.getGroupPrice());
    }

    public OrderReportDto mapOrderDtoListToOrderListDto(List<OrderDto> orderDtos) {
        OrderReportDto orderReportDto = new OrderReportDto();
        for (OrderDto orderDto : orderDtos) {
            orderReportDto.addToOrderList(orderDto);
            orderReportDto.setTotalListPrice(Price.add(orderReportDto.getTotalListPrice(), orderDto.getValue()));
        }
        return orderReportDto;
    }
}
