package com.pon02.Assignment10.service;

import com.pon02.Assignment10.entity.Field;
import com.pon02.Assignment10.exception.FieldNotFoundException;
import com.pon02.Assignment10.mapper.FieldMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FieldServiceTest {

  @InjectMocks
  private FieldService fieldService;

  @Mock
  private FieldMapper fieldMapper;

  @Test
  void 全てのフィールドを取得できる() {
    doReturn(List.of(
        new Field(1, "2025/01/29AM", LocalDate.of(2025, 1, 29), LocalDateTime.of(2025, 1, 28, 9, 0, 0)),
        new Field(2, "2025/01/29PM", LocalDate.of(2025, 1, 29), LocalDateTime.of(2025, 1, 28, 10, 0, 0))
    )).when(fieldMapper).findAllFields();

    List<Field> actual = fieldService.findAllFields();

    assertThat(actual).isEqualTo(List.of(
        new Field(1, "2025/01/29AM", LocalDate.of(2025, 1, 29), LocalDateTime.of(2025, 1, 28, 9, 0, 0)),
        new Field(2, "2025/01/29PM", LocalDate.of(2025, 1, 29), LocalDateTime.of(2025, 1, 28, 10, 0, 0))
    ));
    verify(fieldMapper, times(1)).findAllFields();
  }

  @Test
  void フィールドIDでフィールドを取得できる() {
    Field field = new Field(1, "2025/01/29AM", LocalDate.of(2025, 1, 29), LocalDateTime.of(2025, 1, 28, 9, 0, 0));
    doReturn(Optional.of(field)).when(fieldMapper).findFieldById(1);

    Field actual = fieldService.findFieldById(1);

    assertThat(actual).isEqualTo(field);
    verify(fieldMapper, times(1)).findFieldById(1);
  }

  @Test
  void フィールド名でフィールドを取得できる() {
    doReturn(List.of(
        new Field(1, "2025/01/29AM", LocalDate.of(2025, 1, 29), LocalDateTime.of(2025, 1, 28, 9, 0, 0)),
        new Field(2, "2025/01/29PM", LocalDate.of(2025, 1, 29), LocalDateTime.of(2025, 1, 28, 10, 0, 0))
    )).when(fieldMapper).findFieldByName("2025/01/29");

    List<Field> actual = fieldService.findFieldByName("2025/01/29");

    assertThat(actual).isEqualTo(List.of(
        new Field(1, "2025/01/29AM", LocalDate.of(2025, 1, 29), LocalDateTime.of(2025, 1, 28, 9, 0, 0)),
        new Field(2, "2025/01/29PM", LocalDate.of(2025, 1, 29), LocalDateTime.of(2025, 1, 28, 10, 0, 0))
    ));
    verify(fieldMapper, times(1)).findFieldByName("2025/01/29");
  }

  @Test
  void フィールド使用日でフィールドを取得できる() {
    doReturn(List.of(
        new Field(1, "2025/01/29AM", LocalDate.of(2025, 1, 29), LocalDateTime.of(2025, 1, 28, 9, 0, 0)),
        new Field(2, "2025/01/29PM", LocalDate.of(2025, 1, 29), LocalDateTime.of(2025, 1, 28, 10, 0, 0))
    )).when(fieldMapper).findFieldByDateOfUse(LocalDate.of(2025, 1, 29));

    List<Field> actual = fieldService.findFieldByDateOfUse(LocalDate.of(2025, 1, 29));

    assertThat(actual).isEqualTo(List.of(
        new Field(1, "2025/01/29AM", LocalDate.of(2025, 1, 29), LocalDateTime.of(2025, 1, 28, 9, 0, 0)),
        new Field(2, "2025/01/29PM", LocalDate.of(2025, 1, 29), LocalDateTime.of(2025, 1, 28, 10, 0, 0))
    ));
    verify(fieldMapper, times(1)).findFieldByDateOfUse(LocalDate.of(2025, 1, 29));
  }

  @Test
  void フィールドIDが存在しない場合例外がスローされること() {
    doReturn(Optional.empty()).when(fieldMapper).findFieldById(100);

    assertThatThrownBy(() -> fieldService.findFieldById(100))
        .isInstanceOfSatisfying(FieldNotFoundException.class, e -> {
          assertThat(e.getMessage()).isEqualTo("Field not found for id: 100");
        });

    verify(fieldMapper, times(1)).findFieldById(100);
  }

  @Test
  void フィールド名が存在しない場合例外がスローされること() {
    doReturn(List.of()).when(fieldMapper).findFieldByName("NonExistentField");

    assertThatThrownBy(() -> fieldService.findFieldByName("NonExistentField"))
        .isInstanceOfSatisfying(FieldNotFoundException.class, e -> {
          assertThat(e.getMessage()).isEqualTo("Field not found for name: NonExistentField");
        });

    verify(fieldMapper, times(1)).findFieldByName("NonExistentField");
  }

  @Test
  void フィールド使用日が存在しない場合例外がスローされること() {
    doReturn(List.of()).when(fieldMapper).findFieldByDateOfUse(LocalDate.of(2026, 1, 29));

    assertThatThrownBy(() -> fieldService.findFieldByDateOfUse(LocalDate.of(2026, 1, 29)))
        .isInstanceOfSatisfying(FieldNotFoundException.class, e -> {
          assertThat(e.getMessage()).isEqualTo("Field not found for date of use: 2026-01-29");
        });

    verify(fieldMapper, times(1)).findFieldByDateOfUse(LocalDate.of(2026, 1, 29));
  }

  @Test
  void フィールドが正しく1件追加されること() {
    Field field = new Field(null, "2025/01/30AM", LocalDate.of(2025, 1, 30), null);
    doNothing().when(fieldMapper).insertField(field);

    Field actual = fieldService.insertField("2025/01/30AM", LocalDate.of(2025, 1, 30));

    assertThat(actual).isEqualTo(field);
    verify(fieldMapper, times(1)).insertField(field);
  }

  @Test
  void フィールドが正しく1件更新されること() {
    Field existing = new Field(2, "2025/01/29PM", LocalDate.of(2025, 1, 29), LocalDateTime.of(2025, 1, 28, 10, 0, 0));
    Field updated = new Field(2, "2025/01/30PM", LocalDate.of(2025, 1, 30), existing.getCreatedAt());

    doReturn(Optional.of(existing)).when(fieldMapper).findFieldById(2);
    doNothing().when(fieldMapper).updateField(updated);

    Field actual = fieldService.updateField(2, "2025/01/30PM", LocalDate.of(2025, 1, 30));

    assertThat(actual).isEqualTo(updated);
    verify(fieldMapper, times(1)).findFieldById(2);
    verify(fieldMapper, times(1)).updateField(updated);
  }
}
