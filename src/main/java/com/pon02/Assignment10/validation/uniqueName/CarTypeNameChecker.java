package com.pon02.Assignment10.validation.uniqueName;

import com.pon02.Assignment10.mapper.CarTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("carTypeNameChecker")
public class CarTypeNameChecker implements UniqueNameChecker {
  @Autowired
  private CarTypeMapper carTypeMapper;

  @Override
  public boolean existsByName(String name) {
    return carTypeMapper.existsByName(name);
  }

}
