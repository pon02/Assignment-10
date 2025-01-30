package com.pon02.Assignment10.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Order {
    private Integer id;
    private Integer fieldId;
    private Integer carTypeId;
    @Setter
    private Integer orderStatusId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order(Integer id, Integer fieldId, Integer carTypeId, Integer orderStatusId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.fieldId = fieldId;
        this.carTypeId = carTypeId;
        this.orderStatusId = orderStatusId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id &&
                fieldId == order.fieldId &&
                carTypeId == order.carTypeId &&
                orderStatusId == order.orderStatusId &&
                Objects.equals(createdAt, order.createdAt) &&
                Objects.equals(updatedAt, order.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fieldId, carTypeId, orderStatusId, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return String.format("Order{id=%d, fieldId=%d, carTypeId=%d, orderStatusId=%d, createdAt=%s, updatedAt=%s}", id, fieldId, carTypeId, orderStatusId, createdAt, updatedAt);
    }
}
