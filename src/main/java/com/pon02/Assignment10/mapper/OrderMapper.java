package com.pon02.Assignment10.mapper;

import com.pon02.Assignment10.entity.Order;
import com.pon02.Assignment10.validation.existsId.ExistChecker;
import java.util.Optional;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrderMapper extends ExistChecker {
    @Select("SELECT * FROM orders WHERE field_id = #{fieldId}")
    List<Order> findAllOrders(Integer fieldId);

    @Select("SELECT * FROM orders WHERE field_id = #{fieldId} AND id = #{orderId}")
    Optional<Order> findOrderById(@Param("fieldId") Integer fieldId, @Param("orderId") Integer orderId);

    @Select("SELECT EXISTS(SELECT 1 FROM orders WHERE field_id = #{fieldId} AND id = #{orderId})")
    boolean existsById(@Param("fieldId") Integer fieldId, @Param("orderId") Integer orderId);

    @Select("SELECT EXISTS(SELECT 1 FROM orders WHERE car_type_id = #{carTypeId})")
    boolean existsByCarTypeId(Integer carTypeId);


    @Insert("INSERT INTO orders (field_id,car_type_id, order_status_id) VALUES (#{fieldId}, #{carTypeId}, #{orderStatusId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertOrder(Order order);

    @Update("UPDATE orders " +
        "SET field_id = #{fieldId}, " +
        "order_status_id = #{orderStatusId}, " +
        "updated_at = CURRENT_TIMESTAMP " +
        "WHERE id = #{order.id} AND field_id = #{fieldId}")
    void updateOrder(@Param("order") Order order, @Param("fieldId") Integer fieldId);

    @Delete("DELETE FROM orders WHERE id = #{orderId} AND field_id = #{fieldId}")
    void deleteOrder(@Param("fieldId") Integer fieldId, @Param("orderId") Integer orderId);
}
