package com.csu.orderservice.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Orders {
    private Long id;
    private Long userId;
    private BigDecimal total;
    private String name;
    private String phone;
    private String email;
    private Integer paytype;       // 0 未支付 1 已支付
    private LocalDateTime createTime;
}
