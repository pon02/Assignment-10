package com.pon02.Assignment10.entity;

import java.util.Objects;

public class CarType {
    private int id;
    private String carType;
    private int capacity;

    public CarType(int id, String carType, int capacity) {
        this.id = id;
        this.carType = carType;
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public String getCarType() {
        return carType;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarType carType = (CarType) o;
        return id == carType.id && capacity == carType.capacity && Objects.equals(this.carType, carType.carType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, carType, capacity);
    }

    @Override
    public String toString() {
        return String.format("CarType{id=%d, carType='%s', capacity=%d}", id, carType, capacity);
    }
}
