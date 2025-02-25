package cn.mbtt.service.component;

import cn.hutool.json.JSONUtil;
import cn.mbtt.service.domain.dto.CartDTO;
import cn.mbtt.service.mapper.CartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CartSyncConsumer {

    @Autowired
    private CartMapper cartMapper;  // 用于与数据库交互的 Mapper

    // 监听 "cart-sync-queue" 队列
    @RabbitListener(queues = "cart-sync-queue")
    public void receiveCartSyncMessage(String cartData) {
        try {
            // 1. 解析消息
            CartDTO cartDTO = parseCartData(cartData);

            // 2. 更新数据库中的购物车数据
            saveCartToDatabase(cartDTO);

        } catch (Exception e) {
            // 处理异常，消息消费失败时的处理逻辑
            System.err.println("Error processing cart sync message: " + e.getMessage());
            // 这里可以做日志记录，或者重新抛出异常通知系统进行重试等
        }
    }

    /**
     * 将 JSON 格式的购物车数据解析为 CartDTO 对象
     * @param cartData JSON 字符串
     * @return 解析后的 CartDTO
     */
    private CartDTO parseCartData(String cartData) {
        try {
            // 将 JSON 数据转化为 CartDTO 对象
            return JSONUtil.toBean(cartData, CartDTO.class);
        } catch (Exception e) {
            // 如果 JSON 解析失败，可以根据需要进行处理或抛出异常
            throw new RuntimeException("Failed to parse cart data", e);
        }
    }

    /**
     * 将购物车数据保存到数据库
     * @param cartDTO 购物车数据对象
     */
    private void saveCartToDatabase(CartDTO cartDTO) {
        try {
            cartMapper.updateCart(cartDTO);
            System.out.println("Successfully saved cart data to the database for userId: " + cartDTO.getUserId());
        } catch (Exception e) {
            // 处理数据库更新失败的情况
            throw new RuntimeException("Failed to save cart data to the database", e);
        }
    }
}
