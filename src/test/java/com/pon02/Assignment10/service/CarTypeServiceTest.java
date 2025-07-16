package com.pon02.Assignment10.service;

import com.pon02.Assignment10.entity.CarType;
import com.pon02.Assignment10.exception.CarTypeNotFoundException;
import com.pon02.Assignment10.mapper.CarTypeMapper;
import com.pon02.Assignment10.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarTypeServiceTest {

    @InjectMocks
    private CarTypeService carTypeService;

    @Mock
    private CarTypeMapper carTypeMapper;

    @Mock
    private OrderMapper orderMapper;

    @Test
    public void 全てのカータイプを取得できる() {
        doReturn(List.of(
                new CarType(1, "セダン4人", 4),
                new CarType(2, "ハコバン7人", 7)))
                .when(carTypeMapper).findAllCarTypes();
        List<CarType> actual = carTypeService.findAllCarTypes();
        assertThat(actual).isEqualTo(List.of(
                new CarType(1, "セダン4人", 4),
                new CarType(2, "ハコバン7人", 7)));
        verify(carTypeMapper, times(1)).findAllCarTypes();
    }

    @Test
    public void カータイプIDでカータイプを取得できる() {
        doReturn(Optional.of(new CarType(1, "セダン4人", 4)))
                .when(carTypeMapper).findCarTypeById(1);
        CarType actual = carTypeService.findCarTypeById(1);
        assertThat(actual).isEqualTo(new CarType(1, "セダン4人", 4));
        verify(carTypeMapper, times(1)).findCarTypeById(1);
    }

    @Test
    public void カータイプIDが存在しない場合例外がスローされること() {
        doReturn(Optional.empty())
                .when(carTypeMapper).findCarTypeById(100);
        assertThatThrownBy(() -> carTypeService.findCarTypeById(100))
                .isInstanceOfSatisfying(CarTypeNotFoundException.class, e -> {
                    assertThat(e.getMessage()).isEqualTo("Car type not found for id: 100");
                });
        verify(carTypeMapper, times(1)).findCarTypeById(100);
    }

    @Test
    public void カータイプ名でカータイプを取得できる() {
        doReturn(List.of(new CarType(1, "セダン4人", 4)))
                .when(carTypeMapper).findCarTypeByName("セダン4人");
        List<CarType> actual = carTypeService.findCarTypeByName("セダン4人");
        assertThat(actual).isEqualTo(List.of(new CarType(1, "セダン4人", 4)));
        verify(carTypeMapper, times(1)).findCarTypeByName("セダン4人");
    }

    @Test
    public void カータイプ名が存在しない場合例外がスローされること() {
        doReturn(List.of())
                .when(carTypeMapper).findCarTypeByName("セダン10人");
        assertThatThrownBy(() -> carTypeService.findCarTypeByName("セダン10人"))
                .isInstanceOfSatisfying(CarTypeNotFoundException.class, e -> {
                    assertThat(e.getMessage()).isEqualTo("Car type not found for name: セダン10人");
                });
        verify(carTypeMapper, times(1)).findCarTypeByName("セダン10人");
    }

    @Test
    public void カータイプが正しく1件追加されること() {
        CarType carType = new CarType(null,"セダン4人", 4);
        doNothing().when(carTypeMapper).insertCarType(carType);
        CarType actual = carTypeService.insertCarType("セダン4人", 4);
        assertThat(actual).isEqualTo(carType);
        verify(carTypeMapper, times(1)).insertCarType(carType);
    }

    @Test
    public void カータイプが正しく1件更新されること() {
        CarType existingCarType = new CarType(1, "セダン4人", 4);
        CarType updatedCarType = new CarType(1, "ハコバン7人", 7);
        doReturn(Optional.of(existingCarType)).when(carTypeMapper).findCarTypeById(1);
        doNothing().when(carTypeMapper).updateCarType(updatedCarType);
        CarType actual = carTypeService.updateCarType(1, "ハコバン7人", 7);
        assertThat(actual).isEqualTo(updatedCarType);

        verify(carTypeMapper, times(1)).findCarTypeById(1);
        verify(carTypeMapper, times(1)).updateCarType(updatedCarType);
    }

    @Test
    public void カータイプが正しく1件削除されること() {
        CarType existingCarType = new CarType(1, "セダン4人", 4);
        doReturn(Optional.of(existingCarType)).when(carTypeMapper).findCarTypeById(1);
        doNothing().when(carTypeMapper).deleteCarType(1);
        carTypeService.deleteCarType(1);
        verify(carTypeMapper, times(1)).findCarTypeById(1);
        verify(carTypeMapper, times(1)).deleteCarType(1);
    }
}
