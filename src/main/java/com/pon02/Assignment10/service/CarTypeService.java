package com.pon02.Assignment10.service;

import com.pon02.Assignment10.entity.CarType;
import com.pon02.Assignment10.exception.CarTypeNotFoundException;
import com.pon02.Assignment10.form.CarTypeForm;
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

    public CarType findCarTypeById(Integer id) {
        return this.carTypeMapper.findCarTypeById(id)
               .orElseThrow(() -> new CarTypeNotFoundException("Car type not found for id: " + id));
    }

    public CarType insertCarType(CarTypeForm carTypeForm) {
        CarType carType = new CarType(
            null,
            carTypeForm.getCarTypeName(),
            carTypeForm.getCapacity()
        );
        carTypeMapper.insertCarType(carType);
        return carType;
    }

    public CarType updateCarType(CarTypeForm carTypeForm) {
        CarType existingCarType = carTypeMapper.findCarTypeById(carTypeForm.getId())
                                  .orElseThrow(() -> new CarTypeNotFoundException("Car type not found for id: " + carTypeForm.getId()));
        existingCarType.setCarTypeName(carTypeForm.getCarTypeName());
        existingCarType.setCapacity(carTypeForm.getCapacity());
        carTypeMapper.updateCarType(existingCarType);
        return existingCarType;
    }
}
