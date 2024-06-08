package com.pon02.Assignment10.form;

import com.pon02.Assignment10.validation.UniqueName;
import jakarta.validation.constraints.*;

public class CarTypeForm {

    @Size(max = 50, message = "50文字以内で入力してください")
    @NotBlank(message = "必須項目です")
    @UniqueName(message = "この車種名はすでに登録されています")
    private String carTypeName;

    @Min(1)
    @Max(999)
    @NotNull(message = "必須項目です")
    private Integer capacity;

    public CarTypeForm(String carType, Integer capacity) {
        this.carTypeName = carType;
        this.capacity = capacity;
    }

    public String getCarTypeName() {
        return carTypeName;
    }

    public Integer getCapacity() {
        return capacity;
    }
}
