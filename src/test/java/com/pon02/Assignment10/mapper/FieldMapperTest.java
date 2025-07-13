package com.pon02.Assignment10.mapper;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.pon02.Assignment10.entity.Field;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DBRider
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FieldMapperTest {

  @Autowired
  FieldMapper fieldMapper;

  @Test
  @DataSet(value = "datasets/fields/fields.yml")
  @Transactional
  void 全てのフィールドが取得できること() {
    List<Field> fields = fieldMapper.findAllFields();
    LocalDateTime c1 = LocalDateTime.of(2025, 1, 28, 9, 0, 0);
    LocalDateTime c2 = LocalDateTime.of(2025, 1, 29, 9, 0, 0);
    assertThat(fields)
        .hasSize(2)
        .contains(
            new Field(1, "2025/01/29AM", LocalDate.of(2025, 1, 29), c1),
            new Field(2, "2025/01/29PM", LocalDate.of(2025, 1, 29), c2)
        );
  }

  @Test
  @DataSet(value = "datasets/fields/field_empty.yml")
  @Transactional
  void フィールドがない時に空のリストが返されること() {
    assertThat(fieldMapper.findAllFields()).isEmpty();
  }

  @Test
  @DataSet(value = "datasets/fields/fields.yml")
  @Transactional
  void フィールドIDでフィールドが取得できること() {
    Optional<Field> field = fieldMapper.findFieldById(1);
    assertThat(field).contains(new Field(1, "2025/01/29AM", LocalDate.of(2025, 1, 29), LocalDateTime.of(2025, 1, 28, 9, 0, 0)));
  }

  @Test
  @DataSet(value = "datasets/fields/field_empty.yml")
  @Transactional
  void 存在しないフィールドIDを指定した時に空で返すこと() {
    Optional<Field> field = fieldMapper.findFieldById(999);
    assertThat(field).isEmpty();
  }

  @Test
  @DataSet(value = "datasets/fields/fields.yml")
  @Transactional
  void フィールド名でフィールドが取得できること() {
    List<Field> fields = fieldMapper.findFieldByName("2025/01/29AM");
    assertThat(fields)
        .hasSize(1)
        .usingRecursiveComparison()
        .ignoringFields("createdAt")
        .isEqualTo(List.of(new Field(1, "2025/01/29AM", LocalDate.of(2025, 1, 29), LocalDateTime.of(2025, 1, 28, 9, 0, 0))));
  }

  @Test
  @DataSet(value = "datasets/fields/fields.yml")
  @Transactional
  void フィールド使用日でフィールドが取得できること() {
    List<Field> fields = fieldMapper.findFieldByDateOfUse(LocalDate.of(2025, 1, 29));
    assertThat(fields)
        .hasSize(2)
        .usingRecursiveComparison()
        .ignoringFields("createdAt")
        .isEqualTo(List.of(
            new Field(1, "2025/01/29AM", LocalDate.of(2025, 1, 29), LocalDateTime.of(2025, 1, 28, 9, 0, 0)),
            new Field(2, "2025/01/29PM", LocalDate.of(2025, 1, 29), LocalDateTime.of(2025, 1, 28, 9, 0, 0))
        ));
  }

  @Test
  @DataSet(value = "datasets/fields/fields.yml")
  @Transactional
  void フィールドIDでフィールドが存在するか確認できること() {
    assertThat(fieldMapper.existsById(1)).isTrue();
    assertThat(fieldMapper.existsById(999)).isFalse();
  }

  @Test
  @DataSet(value = "datasets/fields/fields.yml")
  @Transactional
  void フィールド名でフィールドが存在するか確認できること() {
    assertThat(fieldMapper.existsByName("2025/01/29AM")).isTrue();
    assertThat(fieldMapper.existsByName("2025/01/30AM")).isFalse();
  }

  @Test
  @DataSet(value = "datasets/fields/fields.yml")
  @Transactional
  void フィールド使用日でフィールドが存在するか確認できること() {
    assertThat(fieldMapper.existsByDateOfUse(LocalDate.of(2025,1,29))).isTrue();
    assertThat(fieldMapper.existsByDateOfUse(LocalDate.of(2026,1,29))).isFalse();
  }

  @Test
  @DataSet(value = "datasets/fields/field_empty.yml")
  @Transactional
  void フィールドが追加されること() {
    Field field = new Field(null, "2025/01/30AM", LocalDate.of(2025, 1, 30), null);
    fieldMapper.insertField(field);
    List<Field> fields = fieldMapper.findAllFields();
    assertThat(fields)
        .hasSize(1)
        .usingRecursiveComparison()
        .ignoringFields("createdAt")
        .isEqualTo(List.of(field));
  }

  @Test
  @DataSet(value = "datasets/fields/fields.yml")
  @Transactional
  void フィールドが更新されること() {
    Field existingField = fieldMapper.findFieldById(2).get();
    Field updatedField = new Field(
        existingField.getId(),
        "2025/01/30PM",
        LocalDate.of(2025, 1, 30),
        existingField.getCreatedAt()
    );
    fieldMapper.updateField(updatedField);
    List<Field> fields = fieldMapper.findAllFields();
    assertThat(fields)
        .hasSize(2)
        .isEqualTo(List.of(
            new Field(1, "2025/01/29AM", LocalDate.of(2025, 1, 29), LocalDateTime.of(2025, 1, 28, 9, 0, 0)),
            updatedField
        ));
  }
}
