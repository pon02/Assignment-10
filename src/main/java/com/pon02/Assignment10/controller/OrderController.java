package com.pon02.Assignment10.controller;

import com.pon02.Assignment10.controller.response.Response;
import com.pon02.Assignment10.entity.Order;
import com.pon02.Assignment10.form.OrderForm;
import com.pon02.Assignment10.service.OrderService;
import com.pon02.Assignment10.validation.validationGroup.Create;
import com.pon02.Assignment10.validation.validationGroup.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/fields/{fieldId}/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @GetMapping
    public List<Order> getAllOrders(@PathVariable Integer fieldId) {
        return orderService.findAllOrders(fieldId);
    }

    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable Integer fieldId, @PathVariable Integer orderId) {
        return orderService.findOrderById(fieldId, orderId);
    }

    @PostMapping
    public ResponseEntity<Response> create(@PathVariable Integer fieldId, @RequestBody @Validated(Create.class) OrderForm orderForm, UriComponentsBuilder uriBuilder) {
        Order order = orderService.insertOrder(fieldId, orderForm.getCarTypeId());
        URI location = uriBuilder.path("/fields/{fieldId}/orders/{orderId}").buildAndExpand(fieldId, order.getId()).toUri();
        Response body = new Response("Order created");
        return ResponseEntity.created(location).body(body);
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<Response> update(@PathVariable Integer fieldId, @PathVariable Integer orderId, @RequestBody @Validated(Update.class) OrderForm orderForm, UriComponentsBuilder uriBuilder) {
        Order order = orderService.updateOrder(orderId, fieldId, orderForm.getOrderStatusId());
        URI location = uriBuilder.path("/fields/{fieldId}/orders/{orderId}").buildAndExpand(fieldId, order.getId()).toUri();
        Response body = new Response("Order updated");
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Response> delete(@PathVariable Integer fieldId, @PathVariable Integer orderId) {
        orderService.deleteOrder(fieldId, orderId);
        Response body = new Response("Order deleted");
        return ResponseEntity.ok(body);
    }
}
