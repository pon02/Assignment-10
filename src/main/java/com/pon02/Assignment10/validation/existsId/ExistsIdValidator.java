package com.pon02.Assignment10.validation.existsId;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ExistsIdValidator implements ConstraintValidator<ExistsId, Integer> {

  private final ApplicationContext applicationContext;

  private Class<? extends ExistChecker> checkerClass;

  @Autowired
  public ExistsIdValidator(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
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
    ExistChecker checker = applicationContext.getBean(checkerClass);
    return checker.existsById(id);
  }
}
