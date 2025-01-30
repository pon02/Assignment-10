package com.pon02.Assignment10.service;

import com.pon02.Assignment10.entity.Staff;
import com.pon02.Assignment10.exception.StaffNotFoundException;
import com.pon02.Assignment10.mapper.StaffMapper;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StaffService {
  private final StaffMapper staffMapper;

    public StaffService(StaffMapper staffMapper) {
        this.staffMapper = staffMapper;
    }

    public List<Staff> findAllStaffs(Integer fieldId)  {
        return this.staffMapper.findAllStaffs(fieldId);
    }

    public Staff findStaffById(Integer fieldId, Integer staffId) {
        return this.staffMapper.findStaffById(fieldId,staffId).orElseThrow(() -> new StaffNotFoundException("Staff not found for fieldId: " + fieldId + ", staffId: " + staffId));
    }

    public Staff insertStaff(Integer fieldId, int sectionId) {
        Staff staff = new Staff(null, fieldId, sectionId, 1,null,null);
        staffMapper.insertStaff(staff);
        return staff;
    }

    public Staff updateStaff(Integer staffId, Integer fieldId, Integer staffStatusId) {
        Staff existingStaff = staffMapper.findStaffById(fieldId, staffId).orElseThrow(() -> new StaffNotFoundException("Staff not found for fieldId: " + fieldId + ", staffId: " + staffId));
        Staff updatedStaff = new Staff(
            existingStaff.getId(),
            existingStaff.getFieldId(),
            existingStaff.getSectionId(),
            staffStatusId,
            existingStaff.getCreatedAt(),
            existingStaff.getUpdatedAt()
        );
        staffMapper.updateStaff(updatedStaff, fieldId);
        return updatedStaff;
    }

    public void deleteStaff(Integer fieldId,Integer staffId) {
        staffMapper.findStaffById(fieldId, staffId).orElseThrow(() -> new StaffNotFoundException("Staff not found for fieldId: " + fieldId + ", staffId: " + staffId));
        staffMapper.deleteStaff(fieldId, staffId);
    }

}
