package com.pon02.Assignment10.mapper;

import com.pon02.Assignment10.entity.Staff;
import com.pon02.Assignment10.validation.existsId.ExistChecker;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StaffMapper extends ExistChecker {

  @Select("SELECT * FROM staffs")
  List<Staff> findAllStaffs();

  @Select("SELECT * FROM staffs WHERE id = #{id}")
  Optional<Staff> findStaffById(Integer id);

  @Select("SELECT EXISTS(SELECT 1 FROM staffs WHERE id = #{id})")
  boolean existsById(Integer id);

  @Select("SELECT EXISTS(SELECT 1 FROM staffs WHERE section_id = #{sectionId})")
  boolean existsBySectionId(Integer sectionId);

  @Insert("INSERT INTO staffs (section_id, staff_status_id) VALUES (#{sectionId}, #{staffStatusId})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertStaff(Staff staff);

  @Update("UPDATE staffs " +
      "SET staff_status_id = #{staffStatusId}, " +
      "updated_at = CURRENT_TIMESTAMP " +
      "WHERE id = #{id}")
  void updateStaff(Staff staff);

  @Delete("DELETE FROM staffs WHERE id = #{id}")
  void deleteStaff(Integer id);

}
