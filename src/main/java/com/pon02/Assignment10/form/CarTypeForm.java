package com.pon02.Assignment10.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.pon02.Assignment10.validation.Create;
import com.pon02.Assignment10.validation.UniqueName;
import com.pon02.Assignment10.validation.Update;
import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class CarTypeForm {
    @Min(1)
    @NotNull(message = "必須項目です", groups = {Update.class})
    private Integer id;

    @Size(max = 50, message = "50文字以内で入力してください", groups = {Create.class, Update.class})
    @NotBlank(message = "必須項目です", groups = {Create.class, Update.class})
    @UniqueName(message = "この車種名はすでに登録されています", groups = {Create.class, Update.class})
    private String carTypeName;

    @Min(1)
    @Max(99)
    @NotNull(message = "必須項目です", groups = {Create.class, Update.class})
    private Integer capacity;

    // デフォルトコンストラクタ
    public CarTypeForm() {
    }

    // POST 処理用のコンストラクタ
    public CarTypeForm(String carTypeName, Integer capacity) {
        this.carTypeName = carTypeName;
        this.capacity = capacity;
    }

    // PATCH 処理用のコンストラクタ
    public CarTypeForm(Integer id, String carType, Integer capacity) {
        this.id = id;
        this.carTypeName = carType;
        this.capacity = capacity;
    }
}
