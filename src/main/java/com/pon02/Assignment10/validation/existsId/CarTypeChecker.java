package com.pon02.Assignment10.validation.existsId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.pon02.Assignment10.mapper.CarTypeMapper;

@Component
@Qualifier("carTypeChecker")
public class CarTypeChecker implements ExistChecker {

  @Autowired
  private CarTypeMapper carTypeMapper;

  @Override
  public boolean existsById(Integer id) {
    return carTypeMapper.existsById(id);
  }
}
