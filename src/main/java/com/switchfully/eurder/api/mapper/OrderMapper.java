package com.switchfully.eurder.api.mapper;

import com.switchfully.eurder.domain.Order.*;
import com.switchfully.eurder.domain.Order.dto.CreateOrderDto;
import com.switchfully.eurder.domain.Order.dto.ItemGroupReportDto;
import com.switchfully.eurder.domain.Order.dto.OrderDto;
import com.switchfully.eurder.domain.Order.dto.OrderListDto;
import com.switchfully.eurder.domain.item.Item;
import com.switchfully.eurder.domain.item.Price;
import com.switchfully.eurder.service.ItemService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class OrderMapper {
    private final ItemMapper itemMapper;
    private final ItemService itemService;

    public OrderMapper(ItemMapper itemMapper, ItemService itemService) {
        this.itemMapper = itemMapper;
        this.itemService = itemService;
    }

    public Order mapCreateOrderDtoToOrder(CreateOrderDto createOrderDto) {
        return new Order(itemMapper.mapItemGroupListDtoToItemGroupList(Arrays.stream(createOrderDto.getItemGroups()).toList())
                , createOrderDto.getCustomerId());
    }

    public OrderDto mapOrderToDto(Order order) {
        return new OrderDto()
                .setId(order.getId())
                .setItemGroups(mapItemGroupToItemGroupReportDto(order))
                .setCustomerId(order.getCustomerId())
                .setTotalPrice(order.getTotalPrice());
    }

    private List<ItemGroupReportDto> mapItemGroupToItemGroupReportDto(Order order) {
        List<ItemGroupReportDto> itemGroupReportDtos = new ArrayList<>();
        for (ItemGroup itemGroup : order.getItemGroups()) {
            ItemGroupReportDto itemGroupReportDto = new ItemGroupReportDto();
            Item item = itemGroup.getItem();

            itemGroupReportDto.setItemName(item.getName());
            itemGroupReportDto.setAmount(itemGroup.getAmount());
            itemGroupReportDto.setItemGroupPrice(itemGroup.getGroupPrice());
            itemGroupReportDtos.add(itemGroupReportDto);
        }
        return itemGroupReportDtos;
    }

    public OrderListDto mapOrderDtoListToOrderListDto(List<OrderDto> orderDtos) {
        OrderListDto orderListDto = new OrderListDto();
        for (OrderDto orderDto : orderDtos) {
            orderListDto.addToOrderList(orderDto);
            Price current = orderListDto.getTotalListPrice();
            current.setValue(current.getValue().add(orderDto.getTotalPrice().getValue()).doubleValue());
            orderListDto.setTotalListPrice(current);
        }
        return orderListDto;

    }
}
