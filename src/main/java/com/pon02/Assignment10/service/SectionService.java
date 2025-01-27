package com.pon02.Assignment10.service;

import com.pon02.Assignment10.entity.Section;
import com.pon02.Assignment10.exception.SectionNotFoundException;
import com.pon02.Assignment10.mapper.SectionMapper;
import com.pon02.Assignment10.mapper.StaffMapper;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SectionService {
     private final SectionMapper sectionMapper;
     private final StaffMapper staffMapper;

      public SectionService(SectionMapper sectionMapper, StaffMapper staffMapper) {
          this.sectionMapper = sectionMapper;
          this.staffMapper = staffMapper;
      }

      public List<Section> findAllSections() {
          return this.sectionMapper.findAllSections();
      }

      public Section findSectionById(Integer id) {
          return this.sectionMapper.findSectionById(id)
                .orElseThrow(() -> new SectionNotFoundException("Section not found for id: " + id));
      }

      public Section insertSection(String sectionName) {
          Section section = new Section(null, sectionName);
          sectionMapper.insertSection(section);
          return section;
      }

      public Section updateSection(Integer id, String sectionName) {
          Section existingSection = sectionMapper.findSectionById(id)
              .orElseThrow(() -> new SectionNotFoundException("Section not found for id: " + id));
          existingSection = new Section(id, sectionName);
          sectionMapper.updateSection(existingSection);
          return existingSection;
      }

      public void deleteSection(Integer id) {
          sectionMapper.findSectionById(id)
              .orElseThrow(() -> new SectionNotFoundException("Section not found for id: " + id));
          boolean isUsedInStaffs = staffMapper.existsBySectionId(id);
          if (isUsedInStaffs) {
              throw new IllegalStateException("Cannot delete Section with existing orders");
          }
          sectionMapper.deleteSection(id);
      }

}
