package com.pon02.Assignment10.entity;

import org.springframework.cglib.core.Local;

import java.sql.Timestamp;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Objects;

public class Order {
    private Integer id;
    private Integer carTypeId;
    private Integer orderStatusId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order(Integer id, Integer carTypeId, Integer orderStatusId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.carTypeId = carTypeId;
        this.orderStatusId = orderStatusId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public Integer getId() {
        return id;
    }

    public Integer getCarTypeId() {
        return carTypeId;
    }

    public Integer getOrderStatusId() {
        return orderStatusId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
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
        return String.format("Order{id=%d, carTypeId=%d, orderStatusId=%d, createdAt=%s, updatedAt=%s}", id, carTypeId, orderStatusId, createdAt, updatedAt);
    }
}
