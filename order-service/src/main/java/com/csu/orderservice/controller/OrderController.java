package com.csu.orderservice.controller;

import com.csu.orderservice.dto.OrderDetailsDTO;
import com.csu.orderservice.dto.OrderRequestDTO;
import com.csu.orderservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 创建订单
    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody OrderRequestDTO request) {
        Long orderId = orderService.createOrder(request);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "订单创建成功");
        response.put("orderId", orderId);
        return ResponseEntity.ok(response);
    }

    // 根据 userId 查询订单
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDetailsDTO>> listOrdersByUser(@PathVariable Long userId) {
        List<OrderDetailsDTO> list = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(list);
    }

    // 确认支付
    @PostMapping("/{orderId}/confirm-payment")
    public ResponseEntity<String> confirmPayment(@PathVariable Long orderId) {
        orderService.confirmPayment(orderId);
        return ResponseEntity.ok("{\"message\":\"支付成功\"}");
    }
}
