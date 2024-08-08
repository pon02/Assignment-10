package com.pon02.Assignment10.service;

import com.pon02.Assignment10.entity.Order;
import com.pon02.Assignment10.exception.OrderNotFoundException;
import com.pon02.Assignment10.form.OrderForm;
import com.pon02.Assignment10.mapper.OrderMapper;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderService {
    private final OrderMapper orderMapper;

    public OrderService(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    public List<Order> findAllOrders()  {
        return this.orderMapper.findAllOrders();
    }

    public Order findOrderById(Integer id) {
        return this.orderMapper.findOrderById(id).orElseThrow(() -> new OrderNotFoundException("Order not found for id: " + id));
    }

    public Order insertOrder(OrderForm orderForm) {
        Order order = new Order
            (  null,
                orderForm.getCarTypeId(),
                orderForm.getOrderStatusId(),
                null,
                null
            );
        orderMapper.insertOrder(order);
        return order;
    }

    public Order updateOrder(OrderForm orderForm) {
        Order existingOrder = orderMapper.findOrderById(orderForm.getId()).orElseThrow(() -> new OrderNotFoundException("Order not found for id: " + orderForm.getId()));
        existingOrder.setOrderStatusId(orderForm.getOrderStatusId());
            orderMapper.updateOrder(existingOrder);
        return existingOrder;
    }
}
