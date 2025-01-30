package com.pon02.Assignment10.validation.existsId;

import com.pon02.Assignment10.mapper.FieldMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FieldChecker implements ExistChecker{

  @Autowired
  private FieldMapper fieldMapper;

  @Override
  public boolean existsById(Integer id) {
    return fieldMapper.existsById(id);
  }
}
