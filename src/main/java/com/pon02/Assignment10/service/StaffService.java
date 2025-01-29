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

    public Staff insertStaff(Integer fieldId, int sectionId, int staffStatusId) {
        Staff staff = new Staff(null, fieldId, sectionId, staffStatusId,null,null);
        staffMapper.insertStaff(staff);
        return staff;
    }

    public Staff updateStaff(Integer id, Integer fieldId, Integer staffStatusId) {
        Staff existingStaff = staffMapper.findStaffById(fieldId, id).orElseThrow(() -> new StaffNotFoundException("Staff not found for fieldId: " + fieldId + ", staffId: " + id));
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
