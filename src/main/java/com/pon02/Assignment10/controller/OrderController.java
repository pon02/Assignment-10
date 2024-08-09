package com.pon02.Assignment10.controller;

import com.pon02.Assignment10.controller.response.Response;
import com.pon02.Assignment10.entity.Order;
import com.pon02.Assignment10.form.OrderForm;
import com.pon02.Assignment10.service.OrderService;
import com.pon02.Assignment10.validation.Create;
import com.pon02.Assignment10.validation.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/orders/{id}")
    public Order getOrderById(@PathVariable Integer id) {
        return orderService.findOrderById(id);
    }

    @PostMapping("/orders")
    public ResponseEntity<Response> create(@RequestBody @Validated(Create.class) OrderForm orderForm, UriComponentsBuilder uriBuilder) {
        Order order = orderService.insertOrder(orderForm.getCarTypeId(), orderForm.getOrderStatusId());
        URI location = uriBuilder.path("/orders/{id}").buildAndExpand(order.getId()).toUri();
        Response body = new Response("Order created");
        return ResponseEntity.created(location).body(body);
    }

    @PatchMapping("/orders")
    public ResponseEntity<Response> update(@RequestBody @Validated(Update.class) OrderForm orderForm, UriComponentsBuilder uriBuilder) {
        Order order = orderService.updateOrder(orderForm.getId(), orderForm.getOrderStatusId());
        URI location = uriBuilder.path("/orders/{id}").buildAndExpand(order.getId()).toUri();
        Response body = new Response("Order updated");
        return ResponseEntity.ok(body);
    }
}
