package com.csu.orderservice.dto;

import lombok.Data;

@Data
public class OrderRequestDTO {
    private Long userId;
    private String name;
    private String phone;
    private String email;
    private Integer payType;   // 0 未支付 1 已支付（下单时一般为 0）
}
