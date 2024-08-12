package com.pon02.Assignment10.form;

import com.pon02.Assignment10.validation.existsId.CarTypeChecker;
import com.pon02.Assignment10.validation.validationGroup.Create;
import com.pon02.Assignment10.validation.existsId.ExistsId;
import com.pon02.Assignment10.validation.existsId.OrderChecker;
import com.pon02.Assignment10.validation.validationGroup.Update;
import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class OrderForm {
    @Min(1)
    @NotNull(message = "必須項目です", groups = {Update.class})
    @ExistsId(checker = OrderChecker.class, groups = {Update.class})
    private Integer id;

    @Min(value = 1, message = "1~99の値を入力してください", groups = {Create.class})
    @Max(value = 99, message = "1~99の値を入力してください", groups = {Create.class})
    @NotNull(message = "必須項目です", groups = {Create.class})
    @ExistsId(message = "車種IDが存在しません", checker = CarTypeChecker.class, groups = {Create.class})
    private Integer carTypeId;

    @Min(value = 1, message = "1~4の値を入力してください", groups = {Update.class})
    @Max(value = 4, message = "1~4の値を入力してください", groups = {Update.class})
    @NotNull(message = "必須項目です", groups = {Update.class})
    private Integer orderStatusId = 1;
}
