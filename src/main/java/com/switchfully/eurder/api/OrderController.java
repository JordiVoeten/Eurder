package com.switchfully.eurder.api;

import com.switchfully.eurder.api.mapper.OrderMapper;
import com.switchfully.eurder.domain.Order.Order;
import com.switchfully.eurder.domain.Order.dto.CreateOrderDto;
import com.switchfully.eurder.domain.Order.dto.ItemGroupDto;
import com.switchfully.eurder.domain.Order.dto.OrderDto;
import com.switchfully.eurder.domain.Order.dto.OrderListDto;
import com.switchfully.eurder.domain.item.Price;
import com.switchfully.eurder.security.Feature;
import com.switchfully.eurder.security.UserValidator;
import com.switchfully.eurder.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/orders")
public class OrderController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final OrderMapper orderMapper;
    private final OrderService orderService;
    private final UserValidator userValidator;

    public OrderController(OrderMapper orderMapper, OrderService orderService, UserValidator userValidator) {
        this.orderMapper = orderMapper;
        this.orderService = orderService;
        this.userValidator = userValidator;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Price createOrder(@RequestBody CreateOrderDto createOrderDto, @RequestHeader(required = false) String authorization) {
        logger.info("Method createOrder called");
        userValidator.assertUserTypeForFeature(Feature.ORDER_ITEMS, authorization);
        String id = userValidator.getUserFromAuthorization(authorization).getId();
        Order newOrder = orderMapper.mapCreateOrderDtoToOrder(createOrderDto, id);
        Order savedOrder = orderService.createItem(newOrder);
        OrderDto orderDto = orderMapper.mapOrderToDto(savedOrder);
        logger.info("Method createOrder executed successfully");
        return orderDto.getTotalPrice();
    }

    @PostMapping(path = "/{orderId}", produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Price reorderOrder(@PathVariable("orderId") String orderId, @RequestHeader(required = false) String authorization) {
        logger.info("Method reorderOrder called");
        userValidator.assertUserTypeForFeature(Feature.REORDER_EXISTING_ORDER, authorization);
        String userId = userValidator.getUserFromAuthorization(authorization).getId();
        Order reordered = orderService.reorderOrder(orderId, userId);
        OrderDto orderDto = orderMapper.mapOrderToDto(reordered);
        logger.info("Method reorderOrder executed successfully");
        return orderDto.getTotalPrice();
    }

    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public OrderListDto getOrderReport(@RequestHeader(required = false) String authorization) {
        logger.info("Method getOrderReport called");
        userValidator.assertUserTypeForFeature(Feature.VIEW_ORDER_REPORT, authorization);
        String userId = userValidator.getUserFromAuthorization(authorization).getId();
        List<OrderDto> orderDtoList = orderService.getOrdersByUser(userId).stream()
                .map(orderMapper::mapOrderToDto)
                .toList();
        OrderListDto orderListDto = orderMapper.mapOrderDtoListToOrderListDto(orderDtoList);
        logger.info("Method getOrderReport executed successfully");
        return orderListDto;
    }

    @GetMapping(path = "/shippedToday", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemGroupDto> getItemsThatAreShippedToday(@RequestHeader(required = false) String authorization) {
        logger.info("Method getItemsThatAreShippedToday called");
        userValidator.assertUserTypeForFeature(Feature.ITEMS_SHIPPING_TODAY, authorization);
        List<ItemGroupDto> itemGroupDtoList = orderService.getGroupsShippedToday();
        logger.info("Method getItemsThatAreShippedToday executed successfully");
        return itemGroupDtoList;
    }

}
