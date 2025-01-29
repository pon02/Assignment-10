package com.pon02.Assignment10.validation.existsId;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ExistsIdValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsId {

  String message() default "IDが存在しません";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  Class<? extends ExistChecker> checker();
}
