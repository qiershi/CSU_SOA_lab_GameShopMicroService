package com.csu.orderservice.feign.dto;

import lombok.Data;

@Data
public class ProductSnapshotDTO {

    private Long id;
    private String name;
    private Double price;
    private String picture1;
    private String category;
}

