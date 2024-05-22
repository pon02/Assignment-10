package com.pon02.Assignment10.mapper;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.pon02.Assignment10.entity.Order;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DBRider
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderMapperTest {
    @Autowired
    OrderMapper orderMapper;

    @Test
    @DataSet(value = "datasets/orders.yml")
    @Transactional
    void 全てのオーダーが取得できること() {
        List<Order> orders = orderMapper.findAllOrders();
        assertThat(orders)
                .hasSize(2)
                .contains(
                        new Order(1, 1, 2, LocalDateTime.of(2024,5,2,9,0,0), LocalDateTime.of(2024,5,2,9,5,0)),
                        new Order(2, 2, 1, LocalDateTime.of(2024,5,2,9,2,0), null)
                );
    }

}