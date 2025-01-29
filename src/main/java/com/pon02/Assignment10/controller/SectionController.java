package com.pon02.Assignment10.controller;

import com.pon02.Assignment10.controller.response.Response;
import com.pon02.Assignment10.entity.Section;
import com.pon02.Assignment10.form.SectionForm;
import com.pon02.Assignment10.service.SectionService;
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
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping("/sections")
    public List<Section> getSections() {
        return sectionService.findAllSections();
    }

    @GetMapping("/sections/{id}")
    public Section getSectionById(@PathVariable Integer id) {
        return sectionService.findSectionById(id);
    }

    @GetMapping("/sections/{sectionName}")
    public List<Section> getSectionByName(@PathVariable String sectionName) {
        return sectionService.findSectionByName(sectionName);
    }

    @PostMapping("/sections")
    public ResponseEntity<Response> insert(@RequestBody @Validated(Create.class) SectionForm sectionForm, UriComponentsBuilder uriBuilder) {
        Section section = sectionService.insertSection(sectionForm.getSectionName());
        URI location = uriBuilder.path("/sections/{id}").buildAndExpand(section.getId()).toUri();
        Response body = new Response("Section created");
        return ResponseEntity.created(location).body(body);
    }

    @PatchMapping("/sections")
    public ResponseEntity<Response> update(@RequestBody @Validated(Update.class) SectionForm sectionForm, UriComponentsBuilder uriBuilder) {
        Section section = sectionService.updateSection(sectionForm.getId(), sectionForm.getSectionName());
        URI location = uriBuilder.path("/sections/{id}").buildAndExpand(section.getId()).toUri();
        Response body = new Response("Section updated");
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/sections/{id}")
    public ResponseEntity<Response> delete(@PathVariable Integer id) {
        sectionService.deleteSection(id);
        Response body = new Response("Section deleted");
        return ResponseEntity.ok(body);
    }

}
