package cn.mbtt.service.component;

import cn.hutool.json.JSONUtil;
import cn.mbtt.service.domain.dto.CartDTO;
import cn.mbtt.service.mapper.CartMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CartSyncConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(CartSyncConsumer.class);
    @Autowired
    private CartMapper cartMapper;  // 用于与数据库交互的 Mapper

    // 监听 "cart-sync-queue" 队列
    @RabbitListener(queues = "cart-sync-queue")
    public void receiveCartSyncMessage(String cartData) {
        try {
            LOGGER.info("Received cart sync message: {}", cartData);
            CartDTO cartDTO = parseCartData(cartData);
            saveCartToDatabase(cartDTO);
        } catch (Exception e) {
            LOGGER.error("Error processing cart sync message: {}", cartData, e);
            throw e; // 抛出异常以便重试或进入死信队列
        }
    }
    /**
     * 将 JSON 格式的购物车数据解析为 CartDTO 对象
     * @param cartData JSON 字符串
     * @return 解析后的 CartDTO
     */
    private CartDTO parseCartData(String cartData) {
        try {
            return JSONUtil.toBean(cartData, CartDTO.class);
        } catch (Exception e) {
            LOGGER.error("Failed to parse cart data: {}", cartData, e);
            throw new RuntimeException("Failed to parse cart data", e);
        }
    }

    /**
     * 将购物车数据保存到数据库
     * @param cartDTO 购物车数据对象
     */
    private void saveCartToDatabase(CartDTO cartDTO) {
        try {
            if (cartDTO.getUserId() == null) {
                throw new IllegalArgumentException("UserId cannot be null");
            }
            if (cartDTO.getItems() == null || cartDTO.getItems().isEmpty()) {
                LOGGER.warn("No items to save for userId: {}", cartDTO.getUserId());
                return;
            }
            cartMapper.updateCart(cartDTO);
            LOGGER.info("Successfully saved cart data for userId: {}", cartDTO.getUserId());
        } catch (Exception e) {
            LOGGER.error("Failed to save cart data for cartDTO: {}", cartDTO, e);
            throw new RuntimeException("Failed to save cart data to the database", e);
        }
    }
}
