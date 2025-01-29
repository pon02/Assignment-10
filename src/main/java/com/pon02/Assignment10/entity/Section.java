package com.pon02.Assignment10.entity;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Section {
    private Integer id;
    @Setter
    private String sectionName;

    public Section(Integer id, String sectionName) {
        this.id = id;
        this.sectionName = sectionName;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Section section = (Section) o;
      return id == section.id && Objects.equals(this.sectionName, section.sectionName);
    }

  @Override
  public int hashCode() {
    return Objects.hash(id, sectionName);
  }

  @Override
  public String toString() {
    return String.format("Section{id=%d, SectionName='%s'}", id, sectionName);
  }
}
