package cn.mbtt.service.handler;

import cn.mbtt.common.exception.BizIllegalException;
import cn.mbtt.service.domain.dto.PayReqDTO;
import cn.mbtt.service.domain.vo.BasePayResultVO;
import cn.mbtt.service.domain.vo.BasePayVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

//策略模式
public abstract class AbstractPayStrategyHandler<T extends BasePayVO> {
    //第二种写法，用SpringContextHolder类在哪个包？ + 工厂模式

    @Autowired
    //自动将符合条件的AbstractPayStrategyHandler的实例，以paymentType为键，注入到paymentTypeStrategyMap这个Map中
    private Map<String, AbstractPayStrategyHandler> paymentTypeStrategyMap;

    public BasePayResultVO choosePay(PayReqDTO payReqDTO, String paymentType) {
        try {
            if (StringUtils.isBlank(paymentType)) {
                throw new BizIllegalException("找不到支付方式对应策略");
            }
            T basePayVO = buildPayRequest(payReqDTO);

            //根据paymentType从paymentTypeStrategyMap中获取对应的支付策略处理器abstractPayStrategyHandler
            AbstractPayStrategyHandler abstractPayStrategyHandler = paymentTypeStrategyMap.get(paymentType);

            //调用获取到的处理器的pay方法进行支付处理，得到支付结果resultVO并返回
            BasePayResultVO resultVO = abstractPayStrategyHandler.pay(basePayVO);
            return resultVO;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public abstract BasePayResultVO pay(T basePayVO) throws Exception;

    public abstract String paymentType();

    public abstract T buildPayRequest(PayReqDTO payReqDTO);
}

