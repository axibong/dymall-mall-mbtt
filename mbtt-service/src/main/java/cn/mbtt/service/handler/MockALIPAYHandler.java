package cn.mbtt.service.handler;

import cn.mbtt.service.enums.PaymentTypeEnum;
import org.springframework.stereotype.Service;

/**
 * 支付宝支付策略实现
 */
@Service("ALIPAY")
public class MockALIPAYHandler extends AbstractPayStrategyHandler {

    @Override
    public void pay() {
        // 实现支付宝支付逻辑
        System.out.println("Executing Alipay Payment...");
    }

    @Override
    public String paymentType() {
        return PaymentTypeEnum.ALIPAY.name(); // 返回支付方式的枚举值
    }
}
