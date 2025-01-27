package com.pon02.Assignment10.validation.uniqueName;

import com.pon02.Assignment10.validation.uniqueName.CarTypeNameChecker;
import com.pon02.Assignment10.validation.uniqueName.SectionNameChecker;
import com.pon02.Assignment10.validation.uniqueName.UniqueName;
import com.pon02.Assignment10.validation.uniqueName.UniqueNameChecker;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class UniqueNameValidator implements ConstraintValidator<UniqueName, String> {

    private final UniqueNameChecker carTypeNameChecker;
    private final UniqueNameChecker sectionNameChecker;
    private Class<?extends UniqueNameChecker>checkerClass;


    @Autowired
    public UniqueNameValidator(
        @Qualifier("carTypeNameChecker") UniqueNameChecker carTypeNameChecker,
        @Qualifier("sectionNameChecker") UniqueNameChecker sectionNameChecker) {
        this.carTypeNameChecker = carTypeNameChecker;
        this.sectionNameChecker = sectionNameChecker;
    }

    @Override
    public void initialize(UniqueName constraintAnnotation) {
        this.checkerClass = constraintAnnotation.checker();
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null) {
            return true;
        }
        UniqueNameChecker checker = getCheckerInstance();
        return checker.existsByName(name);
    }

    private UniqueNameChecker getCheckerInstance() {
        if (checkerClass == CarTypeNameChecker.class) {
            return carTypeNameChecker;
        } else if (checkerClass == SectionNameChecker.class) {
            return sectionNameChecker;
        }
        throw new IllegalArgumentException("Unsupported checker class");
    }
}
