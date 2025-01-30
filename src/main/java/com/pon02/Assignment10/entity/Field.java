package com.pon02.Assignment10.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;

@Getter
public class Field {
  private Integer id;
  private String fieldName;
  private LocalDate dateOfUse;
  private LocalDateTime createdAt;

  public Field(Integer id, String fieldName, LocalDate dateOfUse, LocalDateTime createdAt) {
    this.id = id;
    this.fieldName = fieldName;
    this.dateOfUse = dateOfUse;
    this.createdAt = createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Field field = (Field) o;
    return id == field.id &&
        Objects.equals(fieldName, field.fieldName) &&
        Objects.equals(dateOfUse, field.dateOfUse) &&
        Objects.equals(createdAt, field.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fieldName, dateOfUse, createdAt);
  }

  @Override
  public String toString() {
    return String.format("Field{id=%d, name=%s, dateOfUse=%s, createdAt=%s}", id, fieldName, dateOfUse, createdAt);
  }
}
