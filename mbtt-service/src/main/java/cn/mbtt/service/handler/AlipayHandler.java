package cn.mbtt.service.handler;

import cn.mbtt.service.config.AliPayConfig;
import cn.mbtt.service.domain.dto.PayReqDTO;
import cn.mbtt.service.domain.vo.AliPayVO;
import cn.mbtt.service.domain.vo.BasePayResultVO;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

@Service("ALIPAY")
public class AlipayHandler extends AbstractPayStrategyHandler<AliPayVO> {
    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "utf-8";
    private static final String SIGN_TYPE = "RSA2";

    @Resource
    AliPayConfig aliPayConfig;

    @Override
    public BasePayResultVO pay(AliPayVO aliPay) throws AlipayApiException, IOException {
        // 1. 创建支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(
                GATEWAY_URL,
                aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(),
                FORMAT,
                CHARSET,
                aliPayConfig.getAlipayPublicKey(),
                SIGN_TYPE
        );

        // 2. 创建支付请求对象
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        aliPay.setOrderNo(UUID.randomUUID().toString());

        // 业务参数设置
        request.setBizContent("{" +
                "\"out_trade_no\":\"" + aliPay.getOrderNo() + "\"," +
                "\"total_amount\":\"" + aliPay.getAmount() + "\"," +
                "\"subject\":\"" + aliPay.getSubject() + "\"," +
                "\"body\":\"" + aliPay.getBody() + "\"," +
                "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        // 3. 客户端执行请求
        String form = alipayClient.pageExecute(request).getBody();

        // 4. 输出支付表单页面
        BasePayResultVO resultVO = new BasePayResultVO();
        resultVO.setContentType("text/html;charset=" + CHARSET);
        resultVO.setForm(form);
        return resultVO;
    }

    @Override
    public String paymentType() {
        return "ALIPAY";
    }

    @Override
    //build方法把请求到的数据PayReqDTO转换成支付宝支付所需的数据AliPayVO
    public AliPayVO buildPayRequest(PayReqDTO payReqDTO) {
        AliPayVO aliPayVO = new AliPayVO();
        aliPayVO.setOrderNo(String.valueOf(payReqDTO.getOrderId()));
        aliPayVO.setAmount(BigDecimal.valueOf(payReqDTO.getAmount()));
        aliPayVO.setSubject("dymall test goods");
        return aliPayVO;
    }
}
