package com.csu.orderservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDetailsDTO {

    @Data
    public static class OrderItemInfo {
        private String name;
        private BigDecimal price;
        private Integer addCount;
        private Long itemId;
        private Long editionId;
    }

    private Long orderId;
    private BigDecimal total;
    private String name;
    private String phone;
    private String email;
    private Integer paytype;
    private LocalDateTime createTime;

    private List<OrderItemInfo> items;
}
