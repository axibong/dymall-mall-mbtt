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
public abstract class AbstractPayStrategyHandler<T extends BasePayVO>
{
    //第二种写法，用SpringContextHolder类在哪个包？ + 工厂模式
    @Autowired
    private Map<String, AbstractPayStrategyHandler> paymentTypeStrategyMap;

    public BasePayResultVO choosePay(PayReqDTO payReqDTO, String paymentType){
        try {

            if (StringUtils.isBlank(paymentType))
            {
                throw new BizIllegalException("找不到支付方式对应策略");
            }
            T basePayVO = buildPayRequest(payReqDTO);
            AbstractPayStrategyHandler abstractPayStrategyHandler = paymentTypeStrategyMap.get(paymentType);
            BasePayResultVO resultVO = abstractPayStrategyHandler.pay(basePayVO);
            return resultVO;
        }
        catch (Exception e) {

            System.out.println(e.getMessage());
        }
        return null;
    }

    public abstract BasePayResultVO pay(T basePayVO) throws Exception;

    public abstract String paymentType();

    public abstract T buildPayRequest(PayReqDTO payReqDTO);
}

