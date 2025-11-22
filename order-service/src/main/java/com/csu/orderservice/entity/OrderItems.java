package com.csu.orderservice.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItems {
    private Long id;
    private Long orderId;
    private String name;       // 商品+版本名称快照
    private BigDecimal price;  // 下单时价格
    private Integer addCount;
    private Long itemId;
    private Long editionId;
}
