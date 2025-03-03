package cn.mbtt.service.handler;

import cn.mbtt.service.enums.PaymentTypeEnum;
import org.springframework.stereotype.Service;

/**
 * 信用卡支付策略实现
 */
@Service("CREDIT_CARD")
public class MockCreditPayHandler extends AbstractPayStrategyHandler {

    @Override
    public void pay() {
        // 实现支付逻辑
        System.out.println("Executing Credit Card Payment...");
    }

    @Override
    public String paymentType() {
        return "CREDIT_CARD"; // 返回支付方式的枚举值PaymentTypeEnum.CREDIT_CARD.name()
    }
}