package com.pon02.Assignment10.entity;

import java.util.Objects;

public class CarType {
    private Integer id;
    private String carTypeName;
    private Integer capacity;

    public CarType(Integer id, String carTypeName, Integer capacity) {
        this.id = id;
        this.carTypeName = carTypeName;
        this.capacity = capacity;
    }

    public Integer getId() {
        return id;
    }

    public String getCarTypeName() {
        return carTypeName;
    }

    public Integer getCapacity() {
        return capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarType carType = (CarType) o;
        return id == carType.id && capacity == carType.capacity && Objects.equals(this.carTypeName, carType.carTypeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, carTypeName, capacity);
    }

    @Override
    public String toString() {
        return String.format("CarType{id=%d, carType='%s', capacity=%d}", id, carTypeName, capacity);
    }
}
