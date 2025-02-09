package cn.mbtt.service.controller;

import cn.mbtt.service.domain.dto.CartDTO;
import cn.mbtt.service.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用于处理购物车相关的 HTTP 请求
 */

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 获取所有购物车中的商品
     * @return 购物车商品列表
     */
    @GetMapping("/items")
    public List<CartDTO> getCartItems() {
        return cartService.getCartItems();
    }

    /**
     * 向购物车中添加商品
     * @param productId 商品ID
     * @param quantity 商品数量
     */
    @PostMapping("/add")
    public void addItemToCart(@RequestParam Long productId, @RequestParam Integer quantity) {
        cartService.addItemToCart(productId, quantity);
    }

    /**
     * 从购物车中移除商品
     * @param productId 商品ID
     */
    @DeleteMapping("/remove")
    public void removeItemFromCart(@RequestParam Long productId) {
        cartService.removeItemFromCart(productId);
    }
}
