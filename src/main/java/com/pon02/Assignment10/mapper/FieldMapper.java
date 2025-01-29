package com.pon02.Assignment10.mapper;

import com.pon02.Assignment10.entity.Field;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface FieldMapper{

  @Select("SELECT * FROM fields")
  List<Field> findAllFields();

  @Select("SELECT * FROM fields WHERE id = #{id}")
  Optional<Field> findFieldById(Integer id);

  @Select("SELECT * FROM fields WHERE LOWER(field_name) LIKE LOWER(CONCAT('%', #{fieldName}, '%'))")
  List<Field> findFieldByName(String fieldName);

  @Select("SELECT EXISTS(SELECT 1 FROM fields WHERE id = #{id})")
  boolean existsById(Integer id);

  @Select("SELECT EXISTS(SELECT 1 FROM fields WHERE name = #{name})")
  boolean existsByName(String name);

  @Insert("INSERT INTO fields (name, date_of_use) VALUES (#{name}, #{dateOfUse})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertField(Field field);

  @Update("UPDATE fields " +
      "SET name = #{name}, " +
      "date_of_use = #{dateOfUse}, " +
      "WHERE id = #{id}")
  void updateField(Field field);

  @Delete("DELETE FROM fields WHERE id = #{id}")
  void deleteField(Integer id);
}
