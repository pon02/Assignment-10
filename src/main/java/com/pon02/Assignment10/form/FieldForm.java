package com.pon02.Assignment10.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pon02.Assignment10.validation.existsId.ExistsId;
import com.pon02.Assignment10.validation.existsId.FieldChecker;
import com.pon02.Assignment10.validation.validationGroup.Create;
import com.pon02.Assignment10.validation.validationGroup.Update;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class FieldForm {
  @Min(1)
  @NotNull(message = "必須項目です", groups = {Update.class})
  @ExistsId(checker = FieldChecker.class, groups = {Update.class})
  private Integer id;

  @Size(max = 50, message = "50文字以内で入力してください", groups = {Create.class, Update.class})
  @NotBlank(message = "必須項目です", groups = {Create.class, Update.class})
  private String fieldName;

  @NotNull(message = "必須項目です", groups = {Create.class, Update.class})
  @Future(message = "未来の日付を入力してください", groups = {Create.class, Update.class})
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate dateOfUse;
}
