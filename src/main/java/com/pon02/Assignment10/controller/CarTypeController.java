package com.pon02.Assignment10.controller;

import com.pon02.Assignment10.entity.CarType;
import com.pon02.Assignment10.service.CarTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CarTypeController {
    private final CarTypeService carTypeService;

    public CarTypeController(CarTypeService carTypeService) {
        this.carTypeService = carTypeService;
    }

    @GetMapping("/cartypes")
    public List<CarType> getCarTypes() {
        return carTypeService.findAllCarTypes();
    }

    @GetMapping("/cartypes/{id}")
    public CarType getCarTypeById(@PathVariable int id) {
        return carTypeService.findCarTypeById(id);
    }
}
