package com.pon02.Assignment10.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CarTypeForm {

    @Size(max = 50, message = "50文字以内で入力してください")
    @NotBlank(message = "必須項目です")
    private String carTypeName;
    private int capacity;

    public CarTypeForm(String carType, int capacity) {
        this.carTypeName = carType;
        this.capacity = capacity;
    }

    public String getCarTypeName() {
        return carTypeName;
    }

    public int getCapacity() {
        return capacity;
    }
}
