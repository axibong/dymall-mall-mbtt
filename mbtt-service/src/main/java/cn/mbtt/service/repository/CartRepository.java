package cn.mbtt.service.repository;

import cn.mbtt.service.domain.dto.CartDTO;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于模拟购物车商品的存储
 */

@Repository
public class CartRepository {

    // 模拟一个购物车数据库
    private final List<CartDTO> cartDatabase = new ArrayList<>();  // 声明为 final

    /**
     * 查找某个商品在购物车中的信息
     * @param productId 商品ID
     * @return 购物车信息
     */
    public CartDTO findByProductId(Long productId) {
        return cartDatabase.stream()
                .filter(cart -> cart.getProductId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    /**
     * 保存商品到购物车
     * @param cartDTO 购物车数据传输对象
     */
    public void save(CartDTO cartDTO) {
        // 如果商品已存在，则更新商品数量
        cartDatabase.removeIf(cart -> cart.getProductId().equals(cartDTO.getProductId()));
        cartDatabase.add(cartDTO);
    }

    /**
     * 查找所有购物车中的商品
     * @return 购物车商品列表
     */
    public List<CartDTO> findAll() {
        return new ArrayList<>(cartDatabase);
    }

    /**
     * 删除商品
     * @param cartDTO 购物车数据传输对象
     */
    public void delete(CartDTO cartDTO) {
        cartDatabase.remove(cartDTO);
    }

    /**
     * 清空购物车
     */
    public void deleteAll() {
        cartDatabase.clear();
    }
}
