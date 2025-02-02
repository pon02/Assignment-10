package com.pon02.Assignment10.mapper;

import com.pon02.Assignment10.entity.CarType;
import com.pon02.Assignment10.validation.existsId.ExistChecker;
import com.pon02.Assignment10.validation.uniqueName.UniqueNameChecker;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CarTypeMapper extends ExistChecker, UniqueNameChecker {
    @Select("SELECT * FROM car_types")
    List<CarType> findAllCarTypes();

    @Select("SELECT * FROM car_types WHERE id = #{id}")
    Optional<CarType> findCarTypeById(Integer id);

    @Select("SELECT * FROM car_types WHERE LOWER(car_type) LIKE LOWER(CONCAT('%', #{carTypeName}, '%'))")
    List<CarType> findCarTypeByName(String carTypeName);

    @Insert("INSERT INTO car_types (car_type, capacity) VALUES (#{carTypeName}, #{capacity})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertCarType(CarType carType);

    @Select("SELECT EXISTS(SELECT 1 FROM car_types WHERE car_type = #{carTypeName})")
    boolean existsByName(String carTypeName);

    @Select("SELECT EXISTS(SELECT 1 FROM car_types WHERE id = #{id})")
    boolean existsById(Integer id);

    @Update("UPDATE car_types " +
        "SET car_type = #{carTypeName}, " +
        "capacity = #{capacity} " +
        "WHERE id = #{id}")
    void updateCarType(CarType carType);

    @Delete("DELETE FROM car_types WHERE id = #{id}")
    void deleteCarType(Integer id);
}
