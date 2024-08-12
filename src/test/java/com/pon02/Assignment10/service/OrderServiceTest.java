package com.pon02.Assignment10.service;

import com.pon02.Assignment10.entity.Order;
import com.pon02.Assignment10.form.OrderForm;
import com.pon02.Assignment10.mapper.OrderMapper;
import java.util.Optional;
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

    @Test
    public void オーダーが正しく1件追加されること() {
        Order order = new Order(null,1,1,null,null);
        doNothing().when(orderMapper).insertOrder(order);
        Order actual = orderService.insertOrder(1, 1);
        assertThat(actual).isEqualTo(order);
        verify(orderMapper, times(1)).insertOrder(order);
    }

    @Test
    public void オーダーが正しく1件更新されること() {
        Order existingOrder = new Order(1, 1, 1, LocalDateTime.of(2024, 5, 2, 9, 0, 0),
            null);
        Order updatedOrder = new Order(1, 1, 2, LocalDateTime.of(2024, 5, 2, 9, 0, 0),
            null);
        doReturn(Optional.of(existingOrder)).when(orderMapper).findOrderById(1);
        doNothing().when(orderMapper).updateOrder(updatedOrder);
        Order actual = orderService.updateOrder(1, 2);
        assertThat(actual).isEqualTo(updatedOrder);

        verify(orderMapper, times(1)).findOrderById(1);
        verify(orderMapper, times(1)).updateOrder(updatedOrder);
    }
}
