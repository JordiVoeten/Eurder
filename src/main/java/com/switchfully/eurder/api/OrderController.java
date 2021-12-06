package com.switchfully.eurder.api;

import com.switchfully.eurder.api.mapper.OrderMapper;
import com.switchfully.eurder.domain.Order.CreateOrderDto;
import com.switchfully.eurder.domain.Order.Order;
import com.switchfully.eurder.domain.Order.OrderDto;
import com.switchfully.eurder.domain.item.Price;
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

    public OrderController(OrderMapper orderMapper, OrderService orderService) {
        this.orderMapper = orderMapper;
        this.orderService = orderService;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Price createOrder(@RequestBody CreateOrderDto createOrderDto) {
        logger.info("Method createOrder called");

        Order newOrder = orderMapper.mapCreateOrderDtoToOrder(createOrderDto);
        Order savedOrder = orderService.createItem(newOrder);
        OrderDto orderDto = orderMapper.mapOrderToDto(savedOrder);
        logger.info("Method createOrder executed successfully");
        return orderDto.getTotalPrice();
    }

    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getOrders(){
        logger.info("Method getOrders called");
         List<Order> orderList = orderService.getOrders();
         List<OrderDto> orderDtoList = orderList.stream().map(orderMapper::mapOrderToDto).toList();
         logger.info("Method getOrders executed successfully");
         return orderDtoList;
    }

}
