package com.csu.orderservice.service;

import com.csu.orderservice.dto.OrderDetailsDTO;
import com.csu.orderservice.dto.OrderRequestDTO;

import java.util.List;

public interface OrderService {

    Long createOrder(OrderRequestDTO request);

    List<OrderDetailsDTO> getOrdersByUserId(Long userId);

    void confirmPayment(Long orderId);
}
