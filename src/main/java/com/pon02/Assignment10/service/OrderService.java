package com.pon02.Assignment10.service;

import com.pon02.Assignment10.entity.Order;
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
}
