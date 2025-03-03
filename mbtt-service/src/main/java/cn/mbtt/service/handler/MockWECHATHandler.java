package cn.mbtt.service.handler;

import cn.mbtt.service.enums.PaymentTypeEnum;
import org.springframework.stereotype.Service;

/**
 * 微信支付策略实现
 */
@Service("WECHAT")
public class MockWECHATHandler extends AbstractPayStrategyHandler {

    @Override
    public void pay() {
        // 实现微信支付逻辑
        System.out.println("Executing WeChat Payment...");
    }

    @Override
    public String paymentType() {
        return PaymentTypeEnum.WECHAT.name(); // 返回支付方式的枚举值
    }
}
