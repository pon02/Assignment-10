package com.pon02.Assignment10.controller;

import com.pon02.Assignment10.entity.Order;
import com.pon02.Assignment10.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @GetMapping("/orders")
    public List<Order> getOrders()  {
        return orderService.findAllOrders();
    }
}
