package cn.mbtt.service.component;

import cn.hutool.json.JSONUtil;
import cn.mbtt.service.domain.dto.CartDTO;
import cn.mbtt.service.domain.dto.CartItemDTO;
import cn.mbtt.service.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CartSyncProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartSyncProducer.class);

    // 注入 RabbitTemplate 用于发送消息
    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 定义队列名称（可以放在配置类中）
    private static final String CART_SYNC_QUEUE = "cart-sync-queue";

    // 发送购物车同步消息
    public void sendCartSyncMessage(Long userId, String cartData) {
        try {
            rabbitTemplate.convertAndSend(CART_SYNC_QUEUE, cartData);
            LOGGER.info("Message sent to RabbitMQ: {}", cartData);
        } catch (Exception e) {
            LOGGER.error("Failed to send cart sync message: {}", e.getMessage(), e);
            throw e;
        }
    }
}
