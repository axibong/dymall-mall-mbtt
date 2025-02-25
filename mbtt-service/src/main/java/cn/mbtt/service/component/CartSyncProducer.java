package cn.mbtt.service.component;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartSyncProducer {

    // 注入 RabbitTemplate 用于发送消息
    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 定义队列名称（可以放在配置类中）
    private static final String CART_SYNC_QUEUE = "cart-sync-queue";

    // 发送购物车同步消息
    public void sendCartSyncMessage(Long userId, String cartData) {
        try {
            // 创建消息并发送到 RabbitMQ 队列
            // 发送消息到指定的队列（cart-sync-queue）
            rabbitTemplate.convertAndSend(CART_SYNC_QUEUE, cartData);
            System.out.println("Message sent to RabbitMQ: " + cartData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
