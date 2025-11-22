package com.csu.orderservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartDTO {
    private Long userId;
    private Long itemId;
    private Long editionId;
    private double price;
}
