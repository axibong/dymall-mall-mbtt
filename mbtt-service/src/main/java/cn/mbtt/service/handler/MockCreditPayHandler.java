package cn.mbtt.service.handler;

import cn.mbtt.service.enums.PaymentTypeEnum;
import org.springframework.stereotype.Service;

@Service("CREDIT_CARD")
public class MockCreditPayHandler extends AbstractPayStrategyHandler{
    @Override
    public void pay() {

    }

    @Override
    public String paymentType() {
        return PaymentTypeEnum.CREDIT_CARD.name();
    }
}
