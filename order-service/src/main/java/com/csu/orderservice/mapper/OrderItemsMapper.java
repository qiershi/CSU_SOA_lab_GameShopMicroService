package com.csu.orderservice.mapper;

import com.csu.orderservice.entity.OrderItems;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderItemsMapper {

    @Insert("INSERT INTO order_items(order_id, name, price, add_count, item_id, edition_id) " +
            "VALUES(#{orderId}, #{name}, #{price}, #{addCount}, #{itemId}, #{editionId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OrderItems orderItems);

    @Select("SELECT * FROM order_items WHERE order_id = #{orderId}")
    List<OrderItems> findByOrderId(@Param("orderId") Long orderId);
}
