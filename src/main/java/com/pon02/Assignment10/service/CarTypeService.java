package com.pon02.Assignment10.service;

import com.pon02.Assignment10.entity.CarType;
import com.pon02.Assignment10.exception.CarTypeNotFoundException;
import com.pon02.Assignment10.mapper.CarTypeMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarTypeService {
    private final CarTypeMapper carTypeMapper;

    public CarTypeService(CarTypeMapper carTypeMapper) {
        this.carTypeMapper = carTypeMapper;
    }

    public List<CarType> findAllCarTypes() {
        return this.carTypeMapper.CarTypeFindAll();
    }

    public CarType findCarTypeById(int id) {
        Optional<CarType> carType = Optional.ofNullable(this.carTypeMapper.CarTypeFindById(id));
        if (carType.isEmpty()) {
            throw new CarTypeNotFoundException("No car type found for id: " + id);
        } else {
            return carType.get();
        }
    }
}
