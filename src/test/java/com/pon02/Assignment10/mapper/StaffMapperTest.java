package com.pon02.Assignment10.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.pon02.Assignment10.entity.Staff;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

@DBRider
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StaffMapperTest {
  @Autowired
  StaffMapper staffMapper;

  @Test
  @DataSet(value = "datasets/staffs/staffs.yml")
  @Transactional
  void 全てのスタッフが取得できること() {
    List<Staff> staffs = staffMapper.findAllStaffs(1);
    LocalDateTime c1 = LocalDateTime.of(2024, 5, 2, 9, 0, 0);
    LocalDateTime c2 = LocalDateTime.of(2024, 5, 2, 9, 2, 0);
    LocalDateTime u1 = LocalDateTime.of(2024, 5, 2, 9, 5, 0);
    assertThat(staffs)
        .hasSize(2)
        .contains(
            new Staff(1, 1, 1, 2, c1, u1),
            new Staff(2, 1, 2, 1, c2, null)
        );
  }

  @Test
  @DataSet(value = "datasets/staffs/staff_empty.yml")
  @Transactional
  void スタッフ登録がない時に空のリストが返されること() {
    assertThat(staffMapper.findAllStaffs(1)).isEmpty();
  }

  @Test
  @DataSet(value = "datasets/staffs/staffs.yml")
  @Transactional
  void スタッフIDでスタッフが取得できること() {
    Optional<Staff> staff = staffMapper.findStaffById(1, 1);
    assertThat(staff).contains(new Staff(1, 1, 1, 2, LocalDateTime.of(2024, 5, 2, 9, 0, 0),
        LocalDateTime.of(2024, 5, 2, 9, 5, 0)));
  }

  @Test
  @DataSet(value = "datasets/staffs/staff_empty.yml")
  @Transactional
  void 存在しないスタッフIDを指定した時に空で返すこと() {
    Optional<Staff> staff = staffMapper.findStaffById(1, 1);
    assertThat(staff).isEmpty();
  }

  @Test
  @DataSet(value = "datasets/staffs/staffs.yml")
  @Transactional
  void スタッフIDでスタッフが存在するか確認できること() {
    assertThat(staffMapper.existsById(1, 1)).isTrue();
    assertThat(staffMapper.existsById(1, 100)).isFalse();
  }

  @Test
  @DataSet(value = "datasets/staffs/staffs.yml")
  @Transactional
  void セクションIDでスタッフが存在するか確認できること() {
    assertThat(staffMapper.existsBySectionId(1)).isTrue();
    assertThat(staffMapper.existsBySectionId(100)).isFalse();
  }

  @Test
  @DataSet(value = "datasets/staffs/staff_empty.yml")
  @Transactional
  void スタッフが追加されること() {
    Staff staff = new Staff(3, 1, 3, 3, LocalDateTime.of(2024, 5, 2, 9, 0, 0), null);
    staffMapper.insertStaff(staff);
    List<Staff> staffs = staffMapper.findAllStaffs(1);
    assertThat(staffs)
        .hasSize(1)
        .usingRecursiveComparison()
        .ignoringFields("createdAt", "updatedAt")
        .isEqualTo(List.of(staff));
  }

  @Test
  @DataSet(value = "datasets/staffs/staffs.yml")
  @Transactional
  void スタッフが更新されること() {
    Staff existingStaff = staffMapper.findStaffById(1, 2).get();
    Staff updatedStaff = new Staff(
        existingStaff.getId(),
        existingStaff.getFieldId(),
        existingStaff.getSectionId(),
        2,
        existingStaff.getCreatedAt(),
        LocalDateTime.now().withNano(0)
    );
    staffMapper.updateStaff(updatedStaff, 1);
    List<Staff> staffs = staffMapper.findAllStaffs(1);
    assertThat(staffs)
        .hasSize(2)
        .isEqualTo(List.of(new Staff(1, 1, 1, 2, LocalDateTime.of(2024, 5, 2, 9, 0, 0),
                LocalDateTime.of(2024, 5, 2, 9, 5, 0)),
            updatedStaff
        ));
  }

  @Test
  @DataSet(value = "datasets/staffs/staffs.yml")
  @Transactional
  void スタッフが削除されること() {
    staffMapper.deleteStaff(1, 1);
    List<Staff> staffs = staffMapper.findAllStaffs(1);
    assertThat(staffs)
        .hasSize(1)
        .isEqualTo(List.of(new Staff(2, 1, 2, 1, LocalDateTime.of(2024, 5, 2, 9, 2, 0), null)));
  }
}
