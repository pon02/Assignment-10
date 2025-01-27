package com.pon02.Assignment10.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Staff {
  private Integer id;
  private Integer sectionId;
  @Setter
  private Integer staffStatusId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Staff(Integer id, Integer sectionId, Integer staffStatusId, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
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
            sectionId == staff.sectionId &&
            staffStatusId == staff.staffStatusId &&
            createdAt.equals(staff.createdAt) &&
            updatedAt.equals(staff.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, sectionId, staffStatusId, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    return String.format("Staff{id=%d, sectionId=%d, staffStatusId=%d, createdAt=%s, updatedAt=%s}", id, sectionId, staffStatusId, createdAt, updatedAt);
  }
}
