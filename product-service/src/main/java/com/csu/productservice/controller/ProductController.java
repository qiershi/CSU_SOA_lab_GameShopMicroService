package com.csu.productservice.controller;

import com.csu.productservice.entity.Product;
import com.csu.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    //获取商品列表
    @GetMapping("/main")
    public ResponseEntity<List<Product>> productMain(Model model) {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    //获取单个商品信息
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product productDetail(@PathVariable Integer id) {
        return productService.getProductDetail(id);
    }

    // 新增评论
    @PostMapping(value = "/{productId}/comments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addComment(
            @PathVariable Integer productId,
            @RequestParam("content") String content,
            @RequestHeader("X-User-Id") Integer userId) {
        productService.addComment(productId, content, userId);
        return ResponseEntity.ok("评论添加成功");
    }

    @PutMapping("/{productId}/comment/{commentId}/like")
    public ResponseEntity<?> updateLikeCount(
            @PathVariable Integer productId,
            @PathVariable Integer commentId) {
        try {
            boolean updated = productService.updateCommentLikeCount(commentId);
            if (updated) {
                return ResponseEntity.ok().body("点赞更新成功");
            } else {
                return ResponseEntity.status(400).body("点赞更新失败");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("服务器错误: " + e.getMessage());
        }
    }
}

