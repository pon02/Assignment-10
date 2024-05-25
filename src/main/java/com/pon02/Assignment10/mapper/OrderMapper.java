package com.pon02.Assignment10.mapper;

import com.pon02.Assignment10.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface OrderMapper {
    @Select("SELECT * FROM orders")
    List<Order> findAllOrders();
}
