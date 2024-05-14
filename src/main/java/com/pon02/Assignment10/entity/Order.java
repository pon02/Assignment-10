package com.pon02.Assignment10.entity;

import java.util.Objects;

public class Order {
    private int id;
    private int carTypeId;
    private int orderStatusId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Order(int id, int carTypeId, int carStatusId, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.carTypeId = carTypeId;
        this.orderStatusId = carStatusId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public int getCarTypeId() {
        return carTypeId;
    }

    public int getOrderStatusId() {
        return orderStatusId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id &&
                carTypeId == order.carTypeId &&
                orderStatusId == order.orderStatusId &&
                Objects.equals(createdAt, order.createdAt) &&
                Objects.equals(updatedAt, order.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, carTypeId, orderStatusId, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", carTypeId=" + carTypeId +
                ", carStatusId=" + orderStatusId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
