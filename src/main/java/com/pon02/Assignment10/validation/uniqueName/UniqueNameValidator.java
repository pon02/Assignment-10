package com.pon02.Assignment10.validation.uniqueName;

import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UniqueNameValidator implements ConstraintValidator<UniqueName, String> {

    private final ApplicationContext applicationContext;
    private Class<?extends UniqueNameChecker>checkerClass;


    @Autowired
    public UniqueNameValidator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void initialize(UniqueName constraintAnnotation) {
        this.checkerClass = constraintAnnotation.checker();
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null || name.isBlank()) {
            return true;
        }
        UniqueNameChecker checker = applicationContext.getBean(checkerClass);
        return !checker.existsByName(name);
    }
}
