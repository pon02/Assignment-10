package com.pon02.Assignment10.mapper;

import com.pon02.Assignment10.entity.Order;
import com.pon02.Assignment10.entity.Staff;
import com.pon02.Assignment10.validation.existsId.ExistChecker;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StaffMapper extends ExistChecker {

  @Select("SELECT * FROM staffs WHERE field_id = #{fieldId}")
  List<Staff> findAllStaffs(Integer fieldId);

  @Select("SELECT * FROM staffs WHERE field_id = #{fieldId} AND id = #{staffId}")
  Optional<Staff> findStaffById(@Param("fieldId") Integer fieldId, @Param("staffId") Integer staffId);

  @Select("SELECT EXISTS(SELECT 1 FROM staffs WHERE field_id = #{fieldId} AND id = #{staffId}")
  boolean existsById(@Param("fieldId") Integer fieldId, @Param("staffId") Integer staffId);

  @Select("SELECT EXISTS(SELECT 1 FROM staffs WHERE section_id = #{sectionId})")
  boolean existsBySectionId(Integer sectionId);

  @Insert("INSERT INTO staffs (field_id, section_id, staff_status_id) VALUES (#{fieldId}, #{sectionId}, #{staffStatusId})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertStaff(Staff staff);

  @Update("UPDATE staffs " +
      "SET staff_status_id = #{staffStatusId}, " +
      "updated_at = CURRENT_TIMESTAMP " +
      "WHERE id = #{staff.id} AND field_id = #{fieldId}")
  void updateStaff(@Param("staff") Staff staff, @Param("fieldId") Integer fieldId);

  @Delete("DELETE FROM staffs WHERE id = #{staffId} AND field_id = #{fieldId}")
  void deleteStaff(@Param("fieldId") Integer fieldId, @Param("staffId") Integer staffId);

}
