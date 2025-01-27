package com.pon02.Assignment10.validation.existsId;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ExistsIdValidator implements ConstraintValidator<ExistsId, Integer> {

  private final ExistChecker orderChecker;
  private final ExistChecker carTypeChecker;
  private final ExistChecker staffChecker;
  private final ExistChecker sectionChecker;

  private Class<? extends ExistChecker> checkerClass;

  @Autowired
  public ExistsIdValidator(@Qualifier("orderChecker") ExistChecker orderChecker,
      @Qualifier("carTypeChecker") ExistChecker carTypeChecker,@Qualifier("staffChecker") ExistChecker staffChecker,@Qualifier("sectionChecker") ExistChecker sectionChecker) {
    this.orderChecker = orderChecker;
    this.carTypeChecker = carTypeChecker;
    this.staffChecker = staffChecker;
    this.sectionChecker = sectionChecker;
  }

  @Override
  public void initialize(ExistsId constraintAnnotation) {
    this.checkerClass = constraintAnnotation.checker();
  }

  @Override
  public boolean isValid(Integer id, ConstraintValidatorContext context) {
    if (id == null) {
      return true;
    }
    ExistChecker checker = getCheckerInstance();
    return checker.existsById(id);
  }

  private ExistChecker getCheckerInstance() {
    if (checkerClass == OrderChecker.class) {
      return orderChecker;
    } else if (checkerClass == CarTypeChecker.class) {
      return carTypeChecker;
    } else if (checkerClass == StaffChecker.class) {
      return staffChecker;
    } else if (checkerClass == SectionChecker.class) {
      return sectionChecker;
    }
    throw new IllegalArgumentException("Unsupported checker class");
  }
}
