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
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@DBRider
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderMapperTest {
    @Autowired
    OrderMapper orderMapper;

    @Test
    @DataSet(value = "datasets/orders/orders.yml")
    @Transactional
    void 全てのオーダーが取得できること() {
        List<Order> orders = orderMapper.findAllOrders();
        LocalDateTime c1 = LocalDateTime.of(2024, 5, 2, 9, 0, 0);
        LocalDateTime c2 = LocalDateTime.of(2024, 5, 2, 9, 2, 0);
        LocalDateTime u1 = LocalDateTime.of(2024, 5, 2, 9, 5, 0);
        assertThat(orders)
                .hasSize(2)
                .contains(
                        new Order(1, 1,2, c1, u1),
                        new Order(2, 2, 1, c2, null)
                );
    }

    @Test
    @DataSet(value = "datasets/orders/order_empty.yml")
    @Transactional
    void オーダーがない時に空のリストが返されること() {
        assertThat(orderMapper.findAllOrders()).isEmpty();
    }

    @Test
    @DataSet(cleanBefore = true)
    @Transactional
    void オーダーが追加されること() {
        Order order = new Order(1,1);
        orderMapper.insertOrder(order);
        List<Order> orders = orderMapper.findAllOrders();
        assertThat(orders)
                .hasSize(1)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(List.of(order));
    }
}
