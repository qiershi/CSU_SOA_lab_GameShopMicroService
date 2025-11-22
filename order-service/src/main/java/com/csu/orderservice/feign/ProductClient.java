package com.csu.orderservice.feign;

import com.csu.orderservice.feign.dto.ProductSnapshotDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/product/{id}")
    ProductSnapshotDTO getProduct(@PathVariable("id") Long id);
}
