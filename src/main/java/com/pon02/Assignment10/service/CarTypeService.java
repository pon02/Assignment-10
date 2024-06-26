package com.pon02.Assignment10.service;

import com.pon02.Assignment10.entity.CarType;
import com.pon02.Assignment10.exception.CarTypeNotFoundException;
import com.pon02.Assignment10.mapper.CarTypeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarTypeService {
    private final CarTypeMapper carTypeMapper;

    public CarTypeService(CarTypeMapper carTypeMapper) {
        this.carTypeMapper = carTypeMapper;
    }

    public List<CarType> findAllCarTypes() {
        return this.carTypeMapper.findAllCarTypes();
    }

    public CarType findCarTypeById(int id) {
        return this.carTypeMapper.findCarTypeById(id)
               .orElseThrow(() -> new CarTypeNotFoundException("Car type not found for id: " + id));
    }

    public CarType insertCarType(String carTypeName, int capacity) {
        CarType carType = new CarType(carTypeName, capacity);
        carTypeMapper.insertCarType(carType);
        return carType;
    }
}
