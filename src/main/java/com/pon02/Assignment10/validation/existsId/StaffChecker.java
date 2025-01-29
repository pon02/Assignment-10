package com.pon02.Assignment10.validation.existsId;

import com.pon02.Assignment10.mapper.StaffMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StaffChecker implements ExistChecker{
  @Autowired
  private StaffMapper staffMapper;

  @Override
  public boolean existsById(Integer id) {
    return staffMapper.existsById(id);
  }

}
