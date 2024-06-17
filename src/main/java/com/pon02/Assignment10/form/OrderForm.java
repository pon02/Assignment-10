package com.pon02.Assignment10.form;

import jakarta.validation.constraints.*;

public class OrderForm {

    @Min(1)
    @Max(999)
    @NotNull(message = "必須項目です")
    private Integer carTypeId;

    @Min(1)
    @Max(999)
    @NotNull(message = "必須項目です")
    private Integer orderStatusId;

    public OrderForm(Integer carTypeId, Integer orderStatusId) {
        this.carTypeId = carTypeId;
        this.orderStatusId = 1;
    }

    public Integer getCarTypeId() {
        return carTypeId;
    }

    public Integer getOrderStatusId() {
        return orderStatusId;
    }
}
