package com.csu.orderservice.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemEdition {
    private Long id;
    private String editionName;
    private BigDecimal price;
    private Long productId;
    private Integer storage;
}
