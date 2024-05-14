package com.pon02.Assignment10.entity;

import java.util.Objects;

public class OrderStatus {
    private int id;
    private String orderStatus;

    public OrderStatus(int id, String orderStatus) {
        this.id = id;
        this.orderStatus = orderStatus;
    }

    public int getId() {
        return id;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderStatus orderStatus = (OrderStatus) o;
        return id == orderStatus.id &&
                orderStatus.equals(orderStatus.orderStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderStatus);
    }

    @Override
    public String toString() {
        return "OrderStatus{" +
                "id=" + id +
                ", orderStatus='" + orderStatus +
                '}';
    }
}
