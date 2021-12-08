package com.switchfully.eurder.api.mapper;

import com.switchfully.eurder.domain.Order.*;
import com.switchfully.eurder.domain.Order.dto.CreateOrderDto;
import com.switchfully.eurder.domain.Order.dto.ItemGroupReportDto;
import com.switchfully.eurder.domain.Order.dto.OrderDto;
import com.switchfully.eurder.domain.Order.dto.OrderReportDto;
import com.switchfully.eurder.domain.item.Item;
import com.switchfully.eurder.domain.item.Price;
import com.switchfully.eurder.service.ItemService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper {
    private final ItemMapper itemMapper;
    private final ItemService itemService;

    public OrderMapper(ItemMapper itemMapper, ItemService itemService) {
        this.itemMapper = itemMapper;
        this.itemService = itemService;
    }

    public Order mapCreateOrderDtoToOrder(CreateOrderDto createOrderDto, String customerId) {
        return new Order(itemMapper.mapItemGroupListDtoToItemGroupList(createOrderDto.getItemGroups())
                , customerId);
    }

    public OrderDto mapOrderToDto(Order order) {
        return new OrderDto()
                .setId(order.getId())
                .setItemGroupReportDto(mapItemGroupToItemGroupReportDto(order))
                .setValue(order.getTotalPrice().getValue())
                .setCurrency(order.getTotalPrice().getCurrency());
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

    public OrderReportDto mapOrderDtoListToOrderListDto(List<OrderDto> orderDtos) {
        OrderReportDto orderReportDto = new OrderReportDto();
        for (OrderDto orderDto : orderDtos) {
            orderReportDto.addToOrderList(orderDto);
            Price current = orderReportDto.getTotalListPrice();
            current.setValue(current.getValue().add(orderDto.getValue()).doubleValue());
            orderReportDto.setTotalListPrice(current);
        }
        return orderReportDto;

    }
}
