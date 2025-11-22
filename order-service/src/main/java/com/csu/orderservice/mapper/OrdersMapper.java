package com.csu.orderservice.mapper;

import com.csu.orderservice.entity.Orders;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrdersMapper {

    @Insert("INSERT INTO orders(user_id, total, name, phone, email, paytype, datetime, status, state) " +
            "VALUES(#{userId}, #{total}, #{name}, #{phone}, #{email}, #{paytype}, #{datetime}, #{status}, #{state})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Orders orders);

    @Select("SELECT * FROM orders WHERE user_id = #{userId}")
    List<Orders> findByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM orders WHERE id = #{id}")
    Orders findById(@Param("id") Long id);

    @Update("UPDATE orders SET paytype = #{paytype} WHERE id = #{id}")
    void updatePayType(@Param("id") Long id, @Param("paytype") Integer paytype);
}
