package com.csu.orderservice.controller;

import com.csu.orderservice.dto.CartDTO;
import com.csu.orderservice.dto.CartItemDTO;
import com.csu.orderservice.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // 添加到购物车
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody CartDTO req) {
        cartService.addToCart(req);
        return ResponseEntity.ok("success");
    }

    // 查看购物车
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemDTO>> getCartDetails(@PathVariable Long userId) {
        List<CartItemDTO> cartItems = cartService.getCartDetails(userId);
        return ResponseEntity.ok(cartItems);
    }

    // 勾选加入订单项
    @PostMapping("/select")
    public ResponseEntity<?> selectCartItem(@RequestBody List<Long> cartIds) {
        cartService.updateCartSelection(cartIds);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "响应成功");
        return ResponseEntity.ok(response);
    }

    // 删除购物车条目
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteCart(@PathVariable Long id) {
        boolean success = cartService.deleteCartById(id);
        Map<String, Object> result = new HashMap<>();
        if (success) {
            result.put("code", 200);
            result.put("msg", "删除成功");
            return ResponseEntity.ok(result);
        } else {
            result.put("code", 404);
            result.put("msg", "未找到记录");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }
}
