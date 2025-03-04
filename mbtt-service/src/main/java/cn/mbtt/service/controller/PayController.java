package cn.mbtt.service.controller;

import cn.mbtt.common.exception.BizIllegalException;
import cn.mbtt.common.result.Result;
import cn.mbtt.service.domain.dto.PayReqDTO;
import cn.mbtt.service.domain.dto.PayResultDTO;
import cn.mbtt.service.domain.po.Orders;
import cn.mbtt.service.domain.vo.BasePayResultVO;
import cn.mbtt.service.service.PayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@Api(tags = "支付相关接口")
@RestController
public class PayController {

    @Autowired
    private PayService payService;

    @ApiOperation("发起支付")
    @PostMapping("/pay")
    public Result<PayResultDTO> pay(@RequestBody @ApiParam("支付信息") PayReqDTO payReqDTO) {
        //1.参数校验
        if (payReqDTO.getAmount() <= 0) {
            throw new BizIllegalException("付款金额异常");
        }
        //2.调用service
        BasePayResultVO resultVO = payService.pay(payReqDTO);
        PayResultDTO resultDTO = new PayResultDTO();
        resultDTO.setForm(resultVO.getForm());
        resultDTO.setContentType(resultVO.getContentType());
        return Result.success(resultDTO);
        //此处应该再用一个if判断支付类型，根据不同支付类型返回对应的xxPayResult，再把父类BasePayResult强转成子类的xxPayResult
        //强转逻辑应自己定义一个工具类实现，此处作简化
    }

    @ApiOperation("数据回调")
    @PostMapping("/notify")
    public Result payNotify(HttpServletRequest request) {
        payService.payNotify(request);

        String orderNo = request.getParameter("out_trade_no");
        Orders orders = payService.getNewOrder(orderNo);

        if (orders.getStatus() == 1) {
            return Result.success("支付成功");
        }
        return Result.error("支付失败");
    }
}
