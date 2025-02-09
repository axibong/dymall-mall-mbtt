package cn.mbtt.service.service;

import cn.mbtt.service.domain.dto.CartDTO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于处理购物车的各项业务操作
 */
@Service
public class CartService {

    // 模拟购物车数据（实际要连接数据库）
    private List<CartDTO> cartItems = new ArrayList<>();

    //注释了未使用的Redis缓存：
    //@Cacheable 使用时方法的返回值会被缓存，且下次相同参数调用时会直接从缓存获取，不会执行方法
    //@CacheEvict 用于清除缓存，可以根据指定的缓存键来清除缓存

    /**
     * 添加商品到购物车
     * @param productId 商品ID
     * @param quantity 商品数量
     */
    //@CacheEvict(value = "cart", key = "#productId")  // 清除缓存中的指定商品
    public void addItemToCart(Long productId, Integer quantity) {
        CartDTO existingCartItem = findCartItemByProductId(productId);
        if (existingCartItem == null) {
            // 如果商品不存在，创建一个新的购物车商品
            cartItems.add(new CartDTO(productId, quantity));
        } else {
            // 如果商品已经存在，增加商品数量
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
        }
    }

    /**
     * 根据商品ID查找购物车
     * @param productId 商品ID
     * @return 购物车目录
     */
    //@Cacheable(value = "cart", key = "#productId")  // 查找商品时，先检查缓存
    private CartDTO findCartItemByProductId(Long productId) {
        for (CartDTO cartItem : cartItems) {
            if (cartItem.getProductId().equals(productId)) {
                return cartItem;
            }
        }
        return null;
    }

    /**
     * 获取所有购物车中的商品
     * @return 购物车商品列表
     */
    //@Cacheable(value = "cart")  // 查询所有购物车商品时，缓存结果
    public List<CartDTO> getCartItems() {
        return cartItems;
    }

    /**
     * 从购物车中移除商品
     * @param productId 商品ID
     */
    //@CacheEvict(value = "cart", key = "#productId")  // 清除缓存中的指定商品项
    public void removeItemFromCart(Long productId) {
        CartDTO cartItem = findCartItemByProductId(productId);
        if (cartItem != null) {
            cartItems.remove(cartItem);
        }
    }
}
