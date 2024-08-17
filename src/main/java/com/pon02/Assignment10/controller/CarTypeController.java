package com.pon02.Assignment10.controller;

import com.pon02.Assignment10.controller.response.Response;
import com.pon02.Assignment10.entity.CarType;
import com.pon02.Assignment10.form.CarTypeForm;
import com.pon02.Assignment10.service.CarTypeService;
import com.pon02.Assignment10.validation.validationGroup.Create;
import com.pon02.Assignment10.validation.validationGroup.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class CarTypeController {
    private final CarTypeService carTypeService;

    public CarTypeController(CarTypeService carTypeService) {
        this.carTypeService = carTypeService;
    }

    @GetMapping("/car-types")
    public List<CarType> getCarTypes() {
        return carTypeService.findAllCarTypes();
    }

    @GetMapping("/car-types/{id}")
    public CarType getCarTypeById(@PathVariable Integer id) {
        return carTypeService.findCarTypeById(id);
    }

    @PostMapping("/car-types")
    public ResponseEntity<Response> insert(@RequestBody @Validated(Create.class) CarTypeForm carTypeForm, UriComponentsBuilder uriBuilder) {
        CarType carType = carTypeService.insertCarType(carTypeForm.getCarTypeName(), carTypeForm.getCapacity());
        URI location = uriBuilder.path("/car-types/{id}").buildAndExpand(carType.getId()).toUri();
        Response body = new Response("CarType created");
        return ResponseEntity.created(location).body(body);
    }

    @PatchMapping("/car-types")
    public ResponseEntity<Response> update(@RequestBody @Validated(Update.class) CarTypeForm carTypeForm, UriComponentsBuilder uriBuilder) {
        CarType carType = carTypeService.updateCarType(carTypeForm.getId(), carTypeForm.getCarTypeName(), carTypeForm.getCapacity());
        URI location = uriBuilder.path("/car-types/{id}").buildAndExpand(carType.getId()).toUri();
        Response body = new Response("CarType updated");
        return ResponseEntity.ok(body);
    }
}
