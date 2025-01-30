package com.pon02.Assignment10.service;

import com.pon02.Assignment10.entity.Order;
import com.pon02.Assignment10.exception.OrderNotFoundException;
import com.pon02.Assignment10.form.OrderForm;
import com.pon02.Assignment10.mapper.OrderMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderService {
    private final OrderMapper orderMapper;

    public OrderService(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    public List<Order> findAllOrders(Integer fieldId) {
        return this.orderMapper.findAllOrders(fieldId);
    }

    public Order findOrderById(Integer fieldId, Integer orderId) {
        return this.orderMapper.findOrderById(fieldId,orderId).orElseThrow(() -> new OrderNotFoundException("Order not found for fieldId: " + fieldId + ", orderId: " + orderId));
    }

    public Order insertOrder(Integer fieldId, int carTypeId) {
        Order order = new Order(null, fieldId, carTypeId, 1,null,null);
        orderMapper.insertOrder(order);
        return order;
    }

    public Order updateOrder(Integer orderId, Integer fieldId, Integer orderStatusId) {
        Order existingOrder = orderMapper.findOrderById(fieldId, orderId).orElseThrow(() -> new OrderNotFoundException("Order not found for fieldId: " + fieldId + " and orderId: " + orderId));
        Order updatedOrder = new Order(
            existingOrder.getId(),
            existingOrder.getFieldId(),
            existingOrder.getCarTypeId(),
            orderStatusId,
            existingOrder.getCreatedAt(),
            existingOrder.getUpdatedAt()
        );
        orderMapper.updateOrder(updatedOrder, fieldId);
        return updatedOrder;
    }

    public void deleteOrder(Integer fieldId,Integer orderId) {
        orderMapper.findOrderById(fieldId,orderId).orElseThrow(() -> new OrderNotFoundException("Order not found for fieldId: " + fieldId + " and orderId: " + orderId));
        orderMapper.deleteOrder(fieldId, orderId);
    }
}
