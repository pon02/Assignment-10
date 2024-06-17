package com.pon02.Assignment10.mapper;

import com.pon02.Assignment10.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface OrderMapper {
    @Select("SELECT * FROM orders")
    List<Order> findAllOrders();

    @Insert("INSERT INTO orders (car_type_id, order_status_id) VALUES (#{carTypeId}, #{orderStatusId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertOrder(Order order);
}
