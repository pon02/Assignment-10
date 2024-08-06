package com.pon02.Assignment10.form;

import com.pon02.Assignment10.validation.Create;
import com.pon02.Assignment10.validation.UniqueName;
import com.pon02.Assignment10.validation.Update;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CarTypeForm {
    @Min(1)
    @NotNull(message = "必須項目です", groups = {Update.class})
    private Integer id;

    @Setter
    @Size(max = 50, message = "50文字以内で入力してください")
    @NotBlank(message = "必須項目です", groups = {Create.class, Update.class})
    @UniqueName(message = "この車種名はすでに登録されています")
    private String carTypeName;

    @Setter
    @Min(1)
    @Max(99)
    @NotNull(message = "必須項目です", groups = {Create.class, Update.class})
    private Integer capacity;

    // デフォルトコンストラクタ
    public CarTypeForm() {
    }

    // POST 処理用のコンストラクタ
    public CarTypeForm(String carType, Integer capacity) {
        this.carTypeName = carType;
        this.capacity = capacity;
    }

    // PATCH 処理用のコンストラクタ
    public CarTypeForm(Integer id, String carType, Integer capacity) {
        this.id = id;
        this.carTypeName = carType;
        this.capacity = capacity;
    }
}
