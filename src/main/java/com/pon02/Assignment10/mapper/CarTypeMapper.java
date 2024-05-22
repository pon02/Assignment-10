package com.pon02.Assignment10.mapper;

import com.pon02.Assignment10.entity.CarType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CarTypeMapper {
    @Select("SELECT * FROM car_types")
    List<CarType> findAllCarTypes();

    @Select("SELECT * FROM car_types WHERE id = #{id}")
    Optional<CarType> carTypeFindById(int id);
}
