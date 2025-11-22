package com.csu.orderservice.mapper;

import com.csu.orderservice.entity.Cart;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CartMapper {

    /**
     * 查询用户所有购物车项
     */
    @Select("SELECT * FROM cart WHERE user_id = #{userId}")
    List<Cart> selectByUserId(Long userId);

    /**
     * 查询用户勾选的购物车项（用于生成订单）
     */
    @Select("SELECT * FROM cart WHERE user_id = #{userId} AND is_selected = 1")
    List<Cart> findSelectedByUserId(Long userId);

    /**
     * 查询用户是否已有同一商品同一版本
     */
    @Select("SELECT * FROM cart " +
            "WHERE user_id = #{userId} AND item_id = #{itemId} AND edition_id = #{editionId} " +
            "LIMIT 1")
    Cart findExistingCartItem(Long userId, Long itemId, Long editionId);

    /**
     * 更新数量与价格
     */
    @Update("UPDATE cart SET add_count = add_count + 1, price = #{price} " +
            "WHERE user_id = #{userId} AND item_id = #{itemId} AND edition_id = #{editionId}")
    void updateCartItem(Long userId, Long itemId, Long editionId, BigDecimal price);

    /**
     * 插入新的购物车记录
     */
    @Insert("INSERT INTO cart(user_id, item_id, edition_id, price, add_count, is_selected) " +
            "VALUES(#{userId}, #{itemId}, #{editionId}, #{price}, #{addCount}, #{isSelected})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Cart cart);

    /**
     * 批量勾选（用于订单生成）
     */
    @Update({
            "<script>",
            "UPDATE cart SET is_selected = 1 WHERE id IN ",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    void updateSelectionByIds(@Param("ids") List<Long> ids);

    /**
     * 删除购物车条目
     */
    @Delete("DELETE FROM cart WHERE id = #{id}")
    int deleteById(@Param("id") Integer id);

    @Delete("DELETE FROM cart WHERE user_id = #{userId}")
    void deleteSelectedByUserId(Long userId);
}
