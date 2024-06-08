package com.pon02.Assignment10.mapper;

import com.pon02.Assignment10.entity.CarType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CarTypeMapper {
    @Select("SELECT * FROM car_types")
    List<CarType> findAllCarTypes();

    @Select("SELECT * FROM car_types WHERE id = #{id}")
    Optional<CarType> findCarTypeById(int id);

    @Insert("INSERT INTO car_types (car_type, capacity) VALUES (#{carTypeName}, #{capacity})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertCarType(CarType carType);

    @Select("SELECT EXISTS(SELECT 1 FROM car_types WHERE car_type = #{carTypeName})")
    boolean existsByName(String carTypeName);
}
