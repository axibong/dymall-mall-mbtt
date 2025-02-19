package cn.mbtt.service.handler;

import cn.mbtt.common.exception.BizIllegalException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public abstract class AbstractPayStrategyHandler
{
    //第二种写法，用SpringContextHolder类在哪个包？ + 工厂模式
    @Autowired
    private Map<String, AbstractPayStrategyHandler> paymentTypeStrategyMap = new HashMap<>();

    public AbstractPayStrategyHandler choose(String paymentType)
    {
        if (StringUtils.isBlank(paymentType))
        {
            throw new BizIllegalException("找不到支付方式对应策略");
        }
        return paymentTypeStrategyMap.get(paymentType);
    }

    public abstract void pay();

    public abstract String paymentType();
}
