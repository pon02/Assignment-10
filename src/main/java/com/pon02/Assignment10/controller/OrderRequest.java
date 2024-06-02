package com.pon02.Assignment10.controller;
public class OrderRequest {
    private int carTypeId;
    private int orderStatusId;

    public OrderRequest(int carTypeId, int orderStatusId) {
        this.carTypeId = carTypeId;
        this.orderStatusId = orderStatusId;
    }

    public int getCarTypeId() {
        return carTypeId;
    }

    public int getOrderStatusId() {
        return orderStatusId;
    }
}
