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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/fields/{fieldId}/staffs")
public class StaffController {
  private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }
    @GetMapping
    public List<Staff> getAllStaffs(@PathVariable Integer fieldId)  {
        return staffService.findAllStaffs(fieldId);
    }

    @GetMapping("/{staffId}")
    public Staff getStaffById(@PathVariable Integer fieldId, @PathVariable Integer staffId) {
        return staffService.findStaffById(fieldId, staffId);
    }

    @PostMapping
    public ResponseEntity<Response> create(@PathVariable Integer fieldId, @RequestBody @Validated(Create.class) StaffForm staffForm, UriComponentsBuilder uriBuilder) {
        Staff staff = staffService.insertStaff(fieldId, staffForm.getSectionId());
        URI location = uriBuilder.path("/fields/{fieldId}/staffs/{staffId}").buildAndExpand(fieldId, staff.getId()).toUri();
        Response body = new Response("Staff created");
        return ResponseEntity.created(location).body(body);
    }

    @PatchMapping("/{staffId}")
    public ResponseEntity<Response> update(@PathVariable Integer fieldId, @PathVariable Integer staffId, @RequestBody @Validated(Update.class) StaffForm staffForm, UriComponentsBuilder uriBuilder) {
        Staff staff = staffService.updateStaff(staffId, fieldId, staffForm.getStaffStatusId());
        URI location = uriBuilder.path("/fields/{fieldId}/staffs/{staffId}").buildAndExpand(fieldId, staff.getId()).toUri();
        Response body = new Response("Staff updated");
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{staffId}")
    public ResponseEntity<Response> delete(@PathVariable Integer fieldId, @PathVariable Integer staffId) {
        staffService.deleteStaff(fieldId, staffId);
        return ResponseEntity.noContent().build();
    }
}
