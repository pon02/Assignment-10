package com.pon02.Assignment10.service;

import com.pon02.Assignment10.entity.Field;
import com.pon02.Assignment10.exception.FieldNotFoundException;
import com.pon02.Assignment10.mapper.FieldMapper;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FieldService {
  private final FieldMapper fieldMapper;

  public FieldService(FieldMapper fieldMapper) {
    this.fieldMapper = fieldMapper;
  }

  public List<Field> findAllFields() {
    return this.fieldMapper.findAllFields();
  }

  public Field findFieldById(Integer id) {
    return this.fieldMapper.findFieldById(id).orElseThrow(() -> new FieldNotFoundException("Field not found for id: " + id));
  }

  public List<Field> findFieldByName(String fieldName) {
    List<Field> fields = this.fieldMapper.findFieldByName(fieldName);
    if (fields.isEmpty()) {
      throw new FieldNotFoundException("Field not found for name: " + fieldName);
    }
    return fields;
  }


  public Field insertField(String name, LocalDate dateOfUse) {
    Field field = new Field(null, name, dateOfUse, null);
    fieldMapper.insertField(field);
    return field;
  }

  public Field updateField(Integer id, String name, LocalDate dateOfUse) {
    Field existingField = fieldMapper.findFieldById(id).orElseThrow(() -> new FieldNotFoundException("Field not found for id: " + id));
    Field updatedField = new Field(
        existingField.getId(),
        name,
        dateOfUse,
        existingField.getCreatedAt()
    );
    fieldMapper.updateField(updatedField);
    return updatedField;
  }

  public void deleteField(Integer id) {
    fieldMapper.findFieldById(id).orElseThrow(() -> new FieldNotFoundException("Field not found for id: " + id));
    fieldMapper.deleteField(id);
  }
}
