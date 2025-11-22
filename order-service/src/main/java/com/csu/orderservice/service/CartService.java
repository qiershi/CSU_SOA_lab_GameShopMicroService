package com.csu.orderservice.service;

import com.csu.orderservice.dto.CartDTO;
import com.csu.orderservice.dto.CartItemDTO;

import java.util.List;

public interface CartService {

    void addToCart(CartDTO req);

    List<CartItemDTO> getCartDetails(Long userId);

    void updateCartSelection(List<Long> cartIds);

    boolean deleteCartById(Long id);
}
