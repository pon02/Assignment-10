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

    public List<Staff> findAllStaffs()  {
        return this.staffMapper.findAllStaffs();
    }

    public Staff findStaffById(Integer id) {
        return this.staffMapper.findStaffById(id).orElseThrow(() -> new StaffNotFoundException("Staff not found for id: " + id));
    }

    public Staff insertStaff(int sectionId, int staffStatusId) {
        Staff staff = new Staff(null,sectionId, staffStatusId,null,null);
        staffMapper.insertStaff(staff);
        return staff;
    }

    public Staff updateStaff(Integer id, Integer staffStatusId) {
        Staff existingStaff = staffMapper.findStaffById(id).orElseThrow(() -> new StaffNotFoundException("Staff not found for id: " + id));
        Staff updatedStaff = new Staff(
            existingStaff.getId(),
            existingStaff.getSectionId(),
            staffStatusId,
            existingStaff.getCreatedAt(),
            existingStaff.getUpdatedAt()
        );
        staffMapper.updateStaff(updatedStaff);
        return updatedStaff;
    }

    public void deleteStaff(Integer id) {
        staffMapper.findStaffById(id).orElseThrow(() -> new StaffNotFoundException("Staff not found for id: " + id));
        staffMapper.deleteStaff(id);
    }

}
