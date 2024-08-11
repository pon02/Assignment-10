package com.pon02.Assignment10.mapper;

import com.pon02.Assignment10.entity.Order;
import java.util.Optional;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrderMapper {
    @Select("SELECT * FROM orders")
    List<Order> findAllOrders();

    @Select("SELECT * FROM orders WHERE id = #{id}")
    Optional<Order> findOrderById(Integer id);

    @Insert("INSERT INTO orders (car_type_id, order_status_id) VALUES (#{carTypeId}, #{orderStatusId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertOrder(Order order);

    @Update("UPDATE orders " +
        "SET order_status_id = #{orderStatusId}, " +
        "updated_at = CURRENT_TIMESTAMP " +
        "WHERE id = #{id}")
    void updateOrder(Order order);
}
