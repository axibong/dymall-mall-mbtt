package cn.mbtt.service.controller;

import cn.mbtt.service.domain.dto.PayReqDTO;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Api(tags = "支付相关接口")
@RestController
public class PayController
{
    public void pay(@RequestBody PayReqDTO payReqDTO)
    {
        //1.参数校验

        //2.调用service
    }
}
