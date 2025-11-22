package com.csu.orderservice.service;

import com.csu.orderservice.dto.CartDTO;
import com.csu.orderservice.dto.CartItemDTO;
import com.csu.orderservice.entity.Cart;
import com.csu.orderservice.feign.ProductClient;
import com.csu.orderservice.feign.dto.ProductSnapshotDTO;
import com.csu.orderservice.mapper.CartMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;
    private final ProductClient productClient;

    public CartServiceImpl(CartMapper cartMapper, ProductClient productClient) {
        this.cartMapper = cartMapper;
        this.productClient = productClient;
    }

    @Override
    public void addToCart(CartDTO req) {
        Long userId = req.getUserId();
        Long itemId = req.getItemId();
        Long editionId = req.getEditionId();
        BigDecimal price = BigDecimal.valueOf(req.getPrice());

        Cart existing = cartMapper.findExistingCartItem(userId, itemId, editionId);
        if (existing != null) {
            cartMapper.updateCartItem(userId, itemId, editionId, price);
        } else {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setItemId(itemId);
            cart.setEditionId(editionId);
            cart.setPrice(price);
            cart.setAddCount(1);
            cart.setIsSelected(0);
            cartMapper.insert(cart);
        }
    }

    @Override
    public List<CartItemDTO> getCartDetails(Long userId) {

        List<Cart> list = cartMapper.selectByUserId(userId);
        List<CartItemDTO> dtoList = new ArrayList<>();

        for (Cart cart : list) {

            ProductSnapshotDTO p = productClient.getProduct(cart.getItemId());

            CartItemDTO dto = new CartItemDTO();
            dto.setCartId(cart.getId());
            dto.setItemId(cart.getItemId());
            dto.setQuantity(cart.getAddCount());

            // 商品信息
            dto.setName(p.getName());
            dto.setPrice(p.getPrice());
            dto.setPicture(p.getPicture1());
            dto.setCategory(p.getCategory());

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public void updateCartSelection(List<Long> cartIds) {
        cartMapper.updateSelectionByIds(cartIds);
    }

    @Override
    public boolean deleteCartById(Integer id) {
        return cartMapper.deleteById(id) > 0;
    }
}
