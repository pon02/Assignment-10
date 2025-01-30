package com.pon02.Assignment10.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Staff {
  private Integer id;
  private Integer fieldId;
  private Integer sectionId;
  @Setter
  private Integer staffStatusId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Staff(Integer id, Integer fieldId, Integer sectionId, Integer staffStatusId, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.fieldId = fieldId;
    this.sectionId = sectionId;
    this.staffStatusId = staffStatusId;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Staff staff = (Staff) o;
    return id == staff.id &&
            fieldId == staff.fieldId &&
            sectionId == staff.sectionId &&
            staffStatusId == staff.staffStatusId &&
            Objects.equals(createdAt, staff.createdAt) &&
            Objects.equals(updatedAt, staff.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fieldId, sectionId, staffStatusId, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    return String.format("Staff{id=%d, fieldId=%d, sectionId=%d, staffStatusId=%d, createdAt=%s, updatedAt=%s}", id, fieldId, sectionId, staffStatusId, createdAt, updatedAt);
  }
}
