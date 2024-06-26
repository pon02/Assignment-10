package com.pon02.Assignment10.service;

import com.pon02.Assignment10.entity.Order;
import com.pon02.Assignment10.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderMapper orderMapper;

    @Test
    public void 全てのオーダーを取得できる() {
        doReturn(List.of(
                new Order(1, 1, 2, LocalDateTime.of(2024,5,2,9,0,0), LocalDateTime.of(2024,5,2,9,5,0)),
                new Order(2, 2, 1, LocalDateTime.of(2024,5,2,9,2,0), null)))
        .when(orderMapper).findAllOrders();
        List<Order> actual = orderService.findAllOrders();
        assertThat(actual).isEqualTo(List.of(
                new Order(1, 1, 2, LocalDateTime.of(2024,5,2,9,0,0), LocalDateTime.of(2024,5,2,9,5,0)),
                new Order(2, 2, 1, LocalDateTime.of(2024,5,2,9,2,0), null)));
        verify(orderMapper,times(1)).findAllOrders();
    }
}
