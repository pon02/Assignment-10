package com.pon02.Assignment10.controller;

import com.pon02.Assignment10.controller.response.Response;
import com.pon02.Assignment10.entity.Field;
import com.pon02.Assignment10.form.FieldForm;
import com.pon02.Assignment10.service.FieldService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class FieldController {
  private final FieldService fieldService;

  public FieldController(FieldService fieldService) {
    this.fieldService = fieldService;
  }

  @GetMapping("/fields")
  public List<Field> getFields() {
    return fieldService.findAllFields();
  }

  @GetMapping("/fields/{id}")
  public Field getFieldById(@PathVariable Integer id) {
    return fieldService.findFieldById(id);
  }

  @GetMapping(value = "/fields", params = "fieldName")
  public List<Field> getFieldByName(@RequestParam String fieldName) {
    return fieldService.findFieldByName(fieldName);
  }

  @PostMapping("/fields")
  public ResponseEntity<Response> insert(@RequestBody @Validated(Create.class) FieldForm fieldForm, UriComponentsBuilder uriBuilder) {
    Field field = fieldService.insertField(fieldForm.getFieldName(), fieldForm.getDateOfUse());
    URI location = uriBuilder.path("/fields/{id}").buildAndExpand(field.getId()).toUri();
    Response body = new Response("Field created");
    return ResponseEntity.created(location).body(body);
  }

  @PatchMapping("/fields")
  public ResponseEntity<Response> update(@RequestBody @Validated(Update.class) FieldForm fieldForm, UriComponentsBuilder uriBuilder) {
    Field field = fieldService.updateField(fieldForm.getId(), fieldForm.getFieldName(), fieldForm.getDateOfUse());
    URI location = uriBuilder.path("/fields/{id}").buildAndExpand(field.getId()).toUri();
    Response body = new Response("Field updated");
    return ResponseEntity.ok(body);
  }

  @DeleteMapping("/fields/{id}")
  public ResponseEntity<Response> delete(@PathVariable Integer id) {
    fieldService.deleteField(id);
    Response body = new Response("Field deleted");
    return ResponseEntity.ok(body);
  }

}
