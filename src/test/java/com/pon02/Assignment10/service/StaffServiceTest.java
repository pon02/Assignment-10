package com.pon02.Assignment10.service;

import com.pon02.Assignment10.entity.Staff;
import com.pon02.Assignment10.exception.StaffNotFoundException;
import com.pon02.Assignment10.mapper.StaffMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StaffServiceTest {

  @InjectMocks
  private StaffService staffService;

  @Mock
  private StaffMapper staffMapper;

  @Test
  void 全てのスタッフを取得できる() {
    doReturn(List.of(
        new Staff(1, 1, 1, 1, LocalDateTime.of(2024, 7, 10, 9, 0, 0), null),
        new Staff(2, 1, 2, 2, LocalDateTime.of(2024, 7, 10, 10, 0, 0), null)
    )).when(staffMapper).findAllStaffs(1);

    List<Staff> actual = staffService.findAllStaffs(1);

    assertThat(actual).isEqualTo(List.of(
        new Staff(1, 1, 1, 1, LocalDateTime.of(2024, 7, 10, 9, 0, 0), null),
        new Staff(2, 1, 2, 2, LocalDateTime.of(2024, 7, 10, 10, 0, 0), null)
    ));
    verify(staffMapper, times(1)).findAllStaffs(1);
  }

  @Test
  void スタッフIDでスタッフを取得できる() {
    Staff staff = new Staff(1, 1, 1, 1, LocalDateTime.of(2024, 7, 10, 9, 0, 0), null);
    doReturn(Optional.of(staff)).when(staffMapper).findStaffById(1, 1);

    Staff actual = staffService.findStaffById(1, 1);

    assertThat(actual).isEqualTo(staff);
    verify(staffMapper, times(1)).findStaffById(1, 1);
  }

  @Test
  void スタッフIDが存在しない場合例外がスローされること() {
    doReturn(Optional.empty()).when(staffMapper).findStaffById(1, 100);

    assertThatThrownBy(() -> staffService.findStaffById(1, 100))
        .isInstanceOfSatisfying(StaffNotFoundException.class, e -> {
          assertThat(e.getMessage()).isEqualTo("Staff not found for fieldId: 1, staffId: 100");
        });

    verify(staffMapper, times(1)).findStaffById(1, 100);
  }

  @Test
  void スタッフが正しく1件追加されること() {
    Staff staff = new Staff(null, 1, 1, 1, null, null);
    doNothing().when(staffMapper).insertStaff(staff);

    Staff actual = staffService.insertStaff(1, 1);

    assertThat(actual).isEqualTo(staff);
    verify(staffMapper, times(1)).insertStaff(staff);
  }

  @Test
  void スタッフが正しく1件更新されること() {
    Staff existingStaff = new Staff(1, 1, 1, 1, LocalDateTime.of(2024, 7, 10, 9, 0, 0), null);
    Staff updatedStaff = new Staff(1, 1, 1, 2, LocalDateTime.of(2024, 7, 10, 9, 0, 0), null);
    doReturn(Optional.of(existingStaff)).when(staffMapper).findStaffById(1, 1);
    doNothing().when(staffMapper).updateStaff(updatedStaff, 1);
    Staff actual = staffService.updateStaff(1, 1, 2);
    assertThat(actual).isEqualTo(updatedStaff);

    verify(staffMapper, times(1)).findStaffById(1, 1);
    verify(staffMapper, times(1)).updateStaff(updatedStaff, 1);
  }

  @Test
  void スタッフが正しく1件削除されること() {
    Staff existingStaff = new Staff(1, 1, 1, 1, LocalDateTime.of(2024, 7, 10, 9, 0, 0), null);
    doReturn(Optional.of(existingStaff)).when(staffMapper).findStaffById(1, 1);
    doNothing().when(staffMapper).deleteStaff(1, 1);
    staffService.deleteStaff(1, 1);

    verify(staffMapper, times(1)).findStaffById(1, 1);
    verify(staffMapper, times(1)).deleteStaff(1, 1);
  }
}
