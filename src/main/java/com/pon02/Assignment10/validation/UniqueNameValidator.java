package com.pon02.Assignment10.validation;

import com.pon02.Assignment10.mapper.CarTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueNameValidator implements ConstraintValidator<UniqueName, String> {

    @Autowired
    private CarTypeMapper carTypeMapper;

    @Override
    public void initialize(UniqueName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return name != null && !carTypeMapper.existsByName(name);
    }
}
