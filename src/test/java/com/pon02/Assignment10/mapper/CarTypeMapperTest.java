package com.pon02.Assignment10.mapper;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.pon02.Assignment10.entity.CarType;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DBRider
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CarTypeMapperTest {

    @Autowired
    CarTypeMapper carTypeMapper;

    @Test
    @DataSet(value = "datasets/car_types/car_types.yml")
    @Transactional
    void 全てのカータイプが取得できること() {
        List<CarType> carTypes = carTypeMapper.findAllCarTypes();
        assertThat(carTypes)
                .hasSize(2)
                .contains(
                        new CarType(1, "セダン4人",4),
                        new CarType(2, "ハコバン7人",7)
                );
    }

    @Test
    @DataSet(value = "datasets/car_types/car_type_empty.yml")
    @Transactional
    void カータイプがない時に空のリストが返されること() {
        assertThat(carTypeMapper.findAllCarTypes()).isEmpty();
    }

    @Test
    @DataSet(value = "datasets/car_types/car_types.yml")
    @Transactional
    void カータイプIDでカータイプが取得できること() {
        Optional<CarType> carType = carTypeMapper.findCarTypeById(1);
        assertThat(carType).contains(new CarType(1, "セダン4人",4));
    }

    @Test
    @DataSet(value = "datasets/car_types/car_type_empty.yml")
    @Transactional
    void 存在しないカータイプIDを指定した時に空で返すこと() {
        Optional<CarType> carType = carTypeMapper.findCarTypeById(100);
        assertThat(carType).isEmpty();
    }

    @Test
    @DataSet(cleanBefore = true)
    @Transactional
    void カータイプが追加されること() {
        CarType carType = new CarType("セダン4人", 4);
        carTypeMapper.insertCarType(carType);
        List<CarType> carTypes = carTypeMapper.findAllCarTypes();
        assertThat(carTypes)
                .hasSize(1)
                .isEqualTo(List.of(carType));
    }
}
