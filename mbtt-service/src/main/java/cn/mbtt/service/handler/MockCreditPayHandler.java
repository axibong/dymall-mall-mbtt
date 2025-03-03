package cn.mbtt.service.handler;

import cn.mbtt.service.domain.dto.PayReqDTO;
import cn.mbtt.service.domain.vo.AliPayVO;
import cn.mbtt.service.domain.vo.BasePayResultVO;
import cn.mbtt.service.domain.vo.BasePayVO;
import cn.mbtt.service.enums.PaymentTypeEnum;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

/**
 * 信用卡支付策略实现
 */
@Service("CREDIT_CARD")
public class MockCreditPayHandler extends AbstractPayStrategyHandler {

    @Override
    public BasePayResultVO pay(BasePayVO basePayVO) {
        // 实现支付逻辑
        System.out.println("Executing Credit Card Payment...");
        return null;
    }

    @Override
    public String paymentType() {
        return "CREDIT_CARD"; // 返回支付方式的枚举值PaymentTypeEnum.CREDIT_CARD.name()
    }

    @Override
    public BasePayVO buildPayRequest(PayReqDTO payReqDTO) {
        return null;
    }
}