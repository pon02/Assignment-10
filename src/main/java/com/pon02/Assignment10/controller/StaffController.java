package com.pon02.Assignment10.controller;

import com.pon02.Assignment10.controller.response.Response;
import com.pon02.Assignment10.entity.Staff;
import com.pon02.Assignment10.form.StaffForm;
import com.pon02.Assignment10.service.StaffService;
import com.pon02.Assignment10.validation.validationGroup.Create;
import com.pon02.Assignment10.validation.validationGroup.Update;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class StaffController {
  private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }
    @GetMapping("/staffs")
    public List<Staff> getStaffs()  {
        return staffService.findAllStaffs();
    }

    @GetMapping("/staffs/{id}")
    public Staff getStaffById(@PathVariable Integer id) {
        return staffService.findStaffById(id);
    }

    @PostMapping("/staffs")
    public ResponseEntity<Response> create(@RequestBody @Validated(Create.class) StaffForm staffForm, UriComponentsBuilder uriBuilder) {
        Staff staff = staffService.insertStaff(staffForm.getSectionId(), staffForm.getStaffStatusId());
        URI location = uriBuilder.path("/staffs/{id}").buildAndExpand(staff.getId()).toUri();
        Response body = new Response("Staff created");
        return ResponseEntity.created(location).body(body);
    }

    @PatchMapping("/staffs")
    public ResponseEntity<Response> update(@RequestBody @Validated(Update.class) StaffForm staffForm, UriComponentsBuilder uriBuilder) {
        Staff staff = staffService.updateStaff(staffForm.getId(), staffForm.getStaffStatusId());
        URI location = uriBuilder.path("/staffs/{id}").buildAndExpand(staff.getId()).toUri();
        Response body = new Response("Staff updated");
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/staffs/{id}")
    public ResponseEntity<Response> delete(@PathVariable Integer id) {
        staffService.deleteStaff(id);
        Response body = new Response("Staff deleted");
        return ResponseEntity.ok(body);
    }
}
