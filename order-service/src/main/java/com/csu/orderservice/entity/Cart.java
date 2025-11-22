package com.csu.orderservice.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Cart {
    private Long id;
    private Long userId;
    private Long itemId;
    private Long editionId;
    private BigDecimal price;
    private Integer addCount;
    private Integer isSelected;   // 0 未选中 1 已选中
}
