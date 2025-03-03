package cn.mbtt.service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // 声明队列
    @Bean
    public Queue cartSyncQueue() {
        return new Queue("cart-sync-queue", true); // 持久化队列
    }
}
