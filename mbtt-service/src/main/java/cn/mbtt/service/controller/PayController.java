package cn.mbtt.service.controller;

import cn.mbtt.common.exception.BizIllegalException;
import cn.mbtt.common.result.Result;
import cn.mbtt.service.domain.dto.PayReqDTO;
import cn.mbtt.service.service.PayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "支付相关接口")
@RestController
public class PayController {

    @Autowired
    private PayService payService;

    @ApiOperation("支付")
    @PostMapping
    public Result<Object> pay(@RequestBody @ApiParam("支付信息") PayReqDTO payReqDTO) {
        //1.参数校验
        if (payReqDTO.getAmount() <= 0) {
            throw new BizIllegalException("付款金额异常");
        }
        //2.调用service
        payService.pay(payReqDTO);
        return Result.success();
    }
}
