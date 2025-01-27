package com.pon02.Assignment10.validation.existsId;

import com.pon02.Assignment10.mapper.SectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("sectionChecker")
public class SectionChecker implements ExistChecker {

  @Autowired
  private SectionMapper sectionMapper;

  @Override
  public boolean existsById(Integer id) {
    return sectionMapper.existsById(id);
  }
}
