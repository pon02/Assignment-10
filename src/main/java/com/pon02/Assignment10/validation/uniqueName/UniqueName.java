package com.pon02.Assignment10.validation.uniqueName;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueNameValidator.class)
public @interface UniqueName {
    String message() default "この名前はすでに登録されています";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends UniqueNameChecker> checker();
}
