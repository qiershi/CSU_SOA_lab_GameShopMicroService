package com.csu.orderservice.service;

import com.csu.orderservice.dto.OrderDetailsDTO;
import com.csu.orderservice.dto.OrderRequestDTO;
import com.csu.orderservice.entity.Cart;
import com.csu.orderservice.entity.OrderItems;
import com.csu.orderservice.entity.Orders;
import com.csu.orderservice.feign.ProductClient;
import com.csu.orderservice.feign.dto.ProductSnapshotDTO;
import com.csu.orderservice.mapper.CartMapper;
import com.csu.orderservice.mapper.OrderItemsMapper;
import com.csu.orderservice.mapper.OrdersMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrdersMapper ordersMapper;
    private final OrderItemsMapper orderItemsMapper;
    private final CartMapper cartMapper;
    private final ProductClient productClient;

    public OrderServiceImpl(OrdersMapper ordersMapper,
                            OrderItemsMapper orderItemsMapper,
                            CartMapper cartMapper,
                            ProductClient productClient) {
        this.ordersMapper = ordersMapper;
        this.orderItemsMapper = orderItemsMapper;
        this.cartMapper = cartMapper;
        this.productClient = productClient;
    }

    @Override
    public Long createOrder(OrderRequestDTO request) {

        Long userId = request.getUserId();

        // ① 查询已勾选的购物车记录
        List<Cart> selectedCarts = cartMapper.findSelectedByUserId(userId);
        if (selectedCarts == null || selectedCarts.isEmpty()) {
            throw new RuntimeException("没有选中的购物车项，无法创建订单");
        }

        // ② 计算总价
        BigDecimal total = BigDecimal.ZERO;
        for (Cart cart : selectedCarts) {
            total = total.add(
                    cart.getPrice().multiply(BigDecimal.valueOf(cart.getAddCount()))
            );
        }

        // ③ 创建订单主表
        Orders orders = new Orders();
        orders.setUserId(userId);
        orders.setTotal(total);
        orders.setName(request.getName());
        orders.setPhone(request.getPhone());
        orders.setEmail(request.getEmail());
        orders.setPaytype(request.getPayType());
        orders.setStatus(1);
        orders.setState("0");
        orders.setDatetime(LocalDateTime.now());

        ordersMapper.insert(orders);

        // ④ 插入订单明细项
        for (Cart cart : selectedCarts) {

            // 调用 Feign 获取商品详情（只有 itemId）
            ProductSnapshotDTO product =
                    productClient.getProduct(cart.getItemId());

            OrderItems item = new OrderItems();
            item.setOrderId(orders.getId());
            item.setItemId(cart.getItemId());
            item.setAddCount(cart.getAddCount());

            // 商品名称
            item.setName(product.getName());

            // 商品价格（BigDecimal）
            item.setPrice(BigDecimal.valueOf(product.getPrice()));

            orderItemsMapper.insert(item);
        }

        // ⑤ 删除购物车中已选 items
        cartMapper.deleteSelectedByUserId(userId);

        return orders.getId();
    }

    @Override
    public List<OrderDetailsDTO> getOrdersByUserId(Long userId) {

        List<Orders> ordersList = ordersMapper.findByUserId(userId);
        List<OrderDetailsDTO> result = new ArrayList<>();

        for (Orders orders : ordersList) {
            OrderDetailsDTO dto = new OrderDetailsDTO();
            dto.setOrderId(orders.getId());
            dto.setTotal(orders.getTotal());
            dto.setName(orders.getName());
            dto.setPhone(orders.getPhone());
            dto.setEmail(orders.getEmail());
            dto.setPaytype(orders.getPaytype());
            dto.setCreateTime(orders.getDatetime());

            List<OrderItems> items = orderItemsMapper.findByOrderId(orders.getId());
            List<OrderDetailsDTO.OrderItemInfo> list = new ArrayList<>();

            for (OrderItems it : items) {
                OrderDetailsDTO.OrderItemInfo info = new OrderDetailsDTO.OrderItemInfo();
                info.setName(it.getName());
                info.setPrice(it.getPrice());
                info.setAddCount(it.getAddCount());
                info.setItemId(it.getItemId());
                list.add(info);
            }

            dto.setItems(list);
            result.add(dto);
        }
        return result;
    }

    @Override
    public void confirmPayment(Long orderId) {
        ordersMapper.updatePayType(orderId, 1);
    }
}
