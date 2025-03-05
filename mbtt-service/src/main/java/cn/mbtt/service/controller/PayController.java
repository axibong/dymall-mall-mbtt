package cn.mbtt.service.controller;

import cn.mbtt.common.exception.BizIllegalException;
import cn.mbtt.common.result.Result;
import cn.mbtt.service.domain.dto.PayReqDTO;
import cn.mbtt.service.service.PayService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "支付相关接口") // 定义控制器的标签
@RestController
public class PayController {

    @Autowired
    private PayService payService;

    @ApiOperation(value = "支付", notes = "发起支付请求，根据支付信息完成支付操作") // 接口描述和详细说明
    @ApiResponses({ // 定义可能的响应
            @ApiResponse(code = 200, message = "支付成功", response = Result.class),
            @ApiResponse(code = 400, message = "参数错误，如金额小于等于0")
    })
    @PostMapping("/pay") // 添加明确路径，建议避免空路径
    public Result<Object> pay(
            @RequestBody // 表示请求体传入
            @ApiParam(value = "支付信息", required = true) // 参数描述
            PayReqDTO payReqDTO) {
        // 1. 参数校验
        if (payReqDTO.getAmount() <= 0) {
            throw new BizIllegalException("付款金额异常");
        }
        // 2. 调用 service
        payService.pay(payReqDTO);
        return Result.success();
    }
}