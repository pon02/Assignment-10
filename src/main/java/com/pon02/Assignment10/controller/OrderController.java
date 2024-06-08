package com.pon02.Assignment10.controller;

import com.pon02.Assignment10.entity.Order;
import com.pon02.Assignment10.form.OrderForm;
import com.pon02.Assignment10.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
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

    @PostMapping("/orders")
    public ResponseEntity<Response> insert(@RequestBody @Validated OrderForm orderForm, UriComponentsBuilder uriBuilder) {
        Order order = orderService.insertOrder(orderForm.getCarTypeId(), orderForm.getOrderStatusId());
        URI location = uriBuilder.path("/orders/{id}").buildAndExpand(order.getId()).toUri();
        Response body = new Response("Order created");
        return ResponseEntity.created(location).body(body);
    }
}
