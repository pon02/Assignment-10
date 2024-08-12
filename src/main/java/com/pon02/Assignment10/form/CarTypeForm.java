package com.pon02.Assignment10.form;

import com.pon02.Assignment10.validation.existsId.CarTypeChecker;
import com.pon02.Assignment10.validation.existsId.ExistsId;
import com.pon02.Assignment10.validation.validationGroup.Create;
import com.pon02.Assignment10.validation.UniqueName;
import com.pon02.Assignment10.validation.validationGroup.Update;
import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class CarTypeForm {
    @Min(1)
    @NotNull(message = "必須項目です", groups = {Update.class})
    @ExistsId(checker = CarTypeChecker.class, groups = {Update.class})
    private Integer id;

    @Size(max = 50, message = "50文字以内で入力してください", groups = {Create.class, Update.class})
    @NotBlank(message = "必須項目です", groups = {Create.class, Update.class})
    @UniqueName(message = "この車種名はすでに登録されています", groups = {Create.class, Update.class})
    private String carTypeName;

    @Min(1)
    @Max(99)
    @NotNull(message = "必須項目です", groups = {Create.class, Update.class})
    private Integer capacity;

}
