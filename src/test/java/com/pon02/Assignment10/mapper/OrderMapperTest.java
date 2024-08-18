package com.pon02.Assignment10.mapper;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.pon02.Assignment10.entity.Order;
import java.util.Optional;
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
    @DataSet(value = "datasets/orders/orders.yml")
    @Transactional
    void オーダーIDでオーダーが取得できること() {
        Optional<Order> order = orderMapper.findOrderById(1);
        assertThat(order).contains(new Order(1, 1, 2, LocalDateTime.of(2024, 5, 2, 9, 0, 0),
            LocalDateTime.of(2024, 5, 2, 9, 5, 0)));
    }

    @Test
    @DataSet(value = "datasets/orders/order_empty.yml")
    @Transactional
    void 存在しないオーダーIDを指定した時に空で返すこと() {
        Optional<Order> order = orderMapper.findOrderById(100);
        assertThat(order).isEmpty();
    }

    @Test
    @DataSet(value = "datasets/orders/orders.yml")
    @Transactional
    void オーダーIDでオーダーが存在するか確認できること() {
        assertThat(orderMapper.existsById(1)).isTrue();
        assertThat(orderMapper.existsById(100)).isFalse();
    }

    @Test
    @DataSet(value = "datasets/orders/orders.yml")
    @Transactional
    void カータイプIDでオーダーが存在するか確認できること() {
        assertThat(orderMapper.existsByCarTypeId(1)).isTrue();
        assertThat(orderMapper.existsByCarTypeId(100)).isFalse();
    }

    @Test
    @DataSet(value = "datasets/orders/order_empty.yml")
    @Transactional
    void オーダーが追加されること() {
        Order order = new Order(null, 4, 1, null, null);
        orderMapper.insertOrder(order);
        List<Order> orders = orderMapper.findAllOrders();
        assertThat(orders)
                .hasSize(1)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(List.of(order));
    }

    @Test
    @DataSet(value = "datasets/orders/orders.yml")
    @Transactional
    void オーダーが更新されること() {
        Order existingOrder = orderMapper.findOrderById(2).get();
        Order updatedOrder = new Order(
            existingOrder.getId(),
            existingOrder.getCarTypeId(),
            2,
            existingOrder.getCreatedAt(),
            LocalDateTime.now().withNano(0)
        );
        orderMapper.updateOrder(updatedOrder);
        List<Order> orders = orderMapper.findAllOrders();
        assertThat(orders)
                .hasSize(2)
            .isEqualTo(List.of(
                new Order(1, 1, 2, LocalDateTime.of(2024, 5, 2, 9, 0, 0), LocalDateTime.of(2024, 5, 2, 9, 5, 0)),
                updatedOrder));
    }
}
