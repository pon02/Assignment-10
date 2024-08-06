package com.pon02.Assignment10.form;

import com.pon02.Assignment10.validation.Create;
import com.pon02.Assignment10.validation.Update;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
public class OrderForm {
    @Min(1)
    @NotNull(message = "必須項目です", groups = {Update.class})
    private Integer id;

    @Min(1)
    @Max(99)
    @NotNull(message = "必須項目です", groups = {Create.class})
    private Integer carTypeId;

    @Setter
    @Min(1)
    @Max(4)
    @NotNull(message = "必須項目です", groups = {Update.class})
    private Integer orderStatusId;

    // POST 処理用のコンストラクタ
    public OrderForm(Integer carTypeId) {
        this.carTypeId = carTypeId;
        this.orderStatusId = 1;
    }

    // PATCH 処理用のコンストラクタ
    public OrderForm(Integer id, Integer orderStatusId) {
        this.id = id;
        this.orderStatusId = orderStatusId;
    }
}
