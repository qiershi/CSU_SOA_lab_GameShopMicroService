package com.csu.orderservice.dto;

import lombok.Data;

@Data
public class CartItemDTO {

    private Long cartId;      // 购物车记录 ID
    private Long itemId;      // 商品 ID
    private Integer quantity; // 数量

    // 商品信息（来自 product-service）
    private String name;
    private Double price;
    private String picture;
    private String category;
}
