package com.pon02.Assignment10.service;

import com.pon02.Assignment10.entity.CarType;
import com.pon02.Assignment10.exception.CarTypeNotFoundException;
import com.pon02.Assignment10.mapper.OrderMapper;
import com.pon02.Assignment10.mapper.CarTypeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarTypeService {
    private final CarTypeMapper carTypeMapper;
    private final OrderMapper orderMapper;

    public CarTypeService(CarTypeMapper carTypeMapper, OrderMapper orderMapper) {
        this.carTypeMapper = carTypeMapper;
        this.orderMapper = orderMapper;
    }

    public List<CarType> findAllCarTypes() {
        return this.carTypeMapper.findAllCarTypes();
    }

    public CarType findCarTypeById(Integer id) {
        return this.carTypeMapper.findCarTypeById(id)
               .orElseThrow(() -> new CarTypeNotFoundException("Car type not found for id: " + id));
    }

    public List<CarType> findCarTypeByName(String carTypeName) {
        List<CarType> carTypes = this.carTypeMapper.findCarTypeByName(carTypeName);
        if (carTypes.isEmpty()) {
            throw new CarTypeNotFoundException("Car type not found for name: " + carTypeName);
        }
        return carTypes;
    }

    public CarType insertCarType(String carTypeName, int capacity) {
        CarType carType = new CarType(null,carTypeName, capacity);
        carTypeMapper.insertCarType(carType);
        return carType;
    }

    public CarType updateCarType(Integer id, String carTypeName, int capacity) {
        CarType existingCarType = carTypeMapper.findCarTypeById(id)
            .orElseThrow(() -> new CarTypeNotFoundException("Car type not found for id: " + id));
        existingCarType = new CarType(id, carTypeName, capacity);
        carTypeMapper.updateCarType(existingCarType);
        return existingCarType;
    }

    public void deleteCarType(Integer id) {
        carTypeMapper.findCarTypeById(id)
            .orElseThrow(() -> new CarTypeNotFoundException("Car type not found for id: " + id));
        boolean isUsedInOrders = orderMapper.existsByCarTypeId(id);
        if (isUsedInOrders) {
            throw new IllegalStateException("Car type with id " + id + " is in use and cannot be deleted");
        }
        carTypeMapper.deleteCarType(id);
    }
}
