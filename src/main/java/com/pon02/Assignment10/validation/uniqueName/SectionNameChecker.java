package com.pon02.Assignment10.validation.uniqueName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.pon02.Assignment10.mapper.SectionMapper;

@Component
@Qualifier("sectionChecker")
public class SectionNameChecker implements UniqueNameChecker {

  @Autowired
  private SectionMapper sectionMapper;

  @Override
public boolean existsByName(String name) {
    return sectionMapper.existsByName(name);
  }
}
