package cn.mbtt.service.service.impl;

import cn.hutool.json.JSONUtil;
import cn.mbtt.service.component.CartSyncProducer;
import cn.mbtt.service.domain.dto.CartFormDTO;
import cn.mbtt.service.domain.po.Users;
import cn.mbtt.service.domain.vo.CartVO;
import cn.mbtt.service.mapper.CartMapper;
import cn.mbtt.service.service.CartService;
import cn.mbtt.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl1 implements CartService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate redisTemplate;  // 用于 Redis 操作
    @Autowired
    private CartSyncProducer cartSyncProducer;  // RabbitMQ 消息生产者

    private static final String CART_PREFIX = "cart:";

    /**
     * 向购物车中添加商品
     * @param cartFormDTO 商品表单DTO
     */
    @Override
    public void addItemToCart(CartFormDTO cartFormDTO) {
        // 1. 获取当前登录用户
        Users currentUser = userService.getCurrentUser();
        String cartKey = CART_PREFIX + currentUser.getId();

        // 2. 检查商品是否已经存在于购物车（先查 Redis）
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        String productIdStr = String.valueOf(cartFormDTO.getProductId());
        if (hashOps.hasKey(cartKey, productIdStr)) {
            // 商品已存在，更新商品数量
            Integer currentQuantity = Integer.valueOf(hashOps.get(cartKey, productIdStr));
            int newQuantity = currentQuantity + cartFormDTO.getQuantity();
            hashOps.put(cartKey, productIdStr, String.valueOf(newQuantity));
        } else {
            // 商品不存在，直接添加
            hashOps.put(cartKey, productIdStr, String.valueOf(cartFormDTO.getQuantity()));
        }

        // 3. 异步同步购物车到数据库
        syncCartToDatabaseAsync(currentUser.getId());
    }

    /**
     * 获取当前用户的购物车商品
     * @return 购物车商品列表
     */
    @Override
    public List<CartVO> getCartItems() {
        // 1. 获取当前登录用户
        Users currentUser = userService.getCurrentUser();
        String cartKey = CART_PREFIX + currentUser.getId();

        // 2. 从 Redis 获取购物车商品的商品ID和数量
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        Map<String, String> cartItems = hashOps.entries(cartKey);  // 获取所有商品及其数量

        // 3. 查询商品详细信息并转换为 CartItemVO
        List<CartVO> cartVOList = new ArrayList<>();
        CartVO cartVO = new CartVO();
        cartVO.setUserId(currentUser.getId()); // 设置当前用户ID

        List<CartVO.CartItemVO> cartItemVOList = new ArrayList<>();

        for (Map.Entry<String, String> entry : cartItems.entrySet()) {
            // 从 Redis 中获取商品ID和数量
            Long productId = Long.valueOf(entry.getKey());
            Integer quantity = Integer.valueOf(entry.getValue());

            // 将商品信息封装到 CartItemVO 中
            CartVO.CartItemVO cartItemVO = new CartVO.CartItemVO();
            cartItemVO.setProductId(productId);
            cartItemVO.setQuantity(quantity);
            cartItemVO.setSelected(true);  // 默认为选中

            // 将 CartItemVO 添加到商品列表中
            cartItemVOList.add(cartItemVO);
        }

        // 设置购物车的商品列表
        cartVO.setItems(cartItemVOList);

        // 计算总金额
        BigDecimal totalAmount = cartItemVOList.stream()
                .map(CartVO.CartItemVO::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cartVO.setTotalAmount(totalAmount);

        // 判断是否全选
        cartVO.setSelectedAll(true);  // 如果所有商品都被选中则设置为 true

        // 将 CartVO 添加到列表中
        cartVOList.add(cartVO);

        return cartVOList;
    }

    /**
     * 移除购物车中的商品
     * @param productId 商品 ID
     */
    @Override
    public void removeItemFromCart(Long productId) {
        // 1. 获取当前登录用户
        Users currentUser = userService.getCurrentUser();
        String cartKey = CART_PREFIX + currentUser.getId();

        // 2. 从 Redis 中移除商品
        redisTemplate.opsForHash().delete(cartKey, String.valueOf(productId));

        // 3. 异步同步购物车数据到数据库
        syncCartToDatabaseAsync(currentUser.getId());
    }

    /**
     * 异步同步购物车到数据库
     * @param userId 用户 ID
     */
    private void syncCartToDatabaseAsync(Long userId) {
        // 从 Redis 中获取购物车数据
        String cartKey = CART_PREFIX + userId;
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        Map<String, String> cartItems = hashOps.entries(cartKey);

        // 将购物车信息转换为 JSON 字符串并发送到 RabbitMQ
        String cartData = convertCartToJson(cartItems);
        cartSyncProducer.sendCartSyncMessage(userId, cartData);  // 发送消息到 RabbitMQ
    }

    /**
     * 将购物车数据转换为 JSON 格式
     * @param cartItems 购物车商品信息
     * @return JSON 字符串
     */
    private String convertCartToJson(Map<String, String> cartItems) {
        // 使用 JSON 序列化工具转换购物车数据为 JSON 字符串
        return JSONUtil.toJsonStr(cartItems);
    }
}
