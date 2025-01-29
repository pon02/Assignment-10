package com.pon02.Assignment10.validation.existsId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.pon02.Assignment10.mapper.OrderMapper;

@Component
public class OrderChecker implements ExistChecker {
  @Autowired
  private OrderMapper orderMapper;

  @Override
  public boolean existsById(Integer id) {
    return orderMapper.existsById(id);
  }
}
