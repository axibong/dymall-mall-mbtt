package cn.mbtt.service.service.impl;

import cn.hutool.db.sql.Order;
import cn.mbtt.service.domain.dto.PayReqDTO;
import cn.mbtt.service.domain.po.Orders;
import cn.mbtt.service.domain.po.PaymentRecords;
import cn.mbtt.service.domain.vo.BasePayResultVO;
import cn.mbtt.service.domain.vo.OrderVO;
import cn.mbtt.service.domain.vo.PayOrderVO;
import cn.mbtt.service.enums.PayStatusEnum;
import cn.mbtt.service.enums.PaymentTypeEnum;
import cn.mbtt.service.handler.AbstractPayStrategyHandler;
import cn.mbtt.service.mapper.OrderMapper;
import cn.mbtt.service.mapper.PayMapper;
import cn.mbtt.service.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.alipay.easysdk.factory.Factory;

public class PayServiceImpl implements PayService {

    @Autowired
    private PayMapper payMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private AbstractPayStrategyHandler abstractPayStrategyHandler;

    @Override
    //返回的VO最好跟传入的DTO同名，此处简化
    public BasePayResultVO pay(PayReqDTO payReqDTO) {
        //1.构建paymentRecords
        PaymentRecords paymentRecords = new PaymentRecords();
        paymentRecords.setOrderId(payReqDTO.getOrderId());
        paymentRecords.setPaymentNo(UUID.randomUUID().toString());//用UUID随机生成一个支付唯一单号
        paymentRecords.setAmount(payReqDTO.getAmount());
        paymentRecords.setPaymentType(payReqDTO.getPaymentType());
        paymentRecords.setStatus(PayStatusEnum.INIT.getCode());
        //2.paymentRecords插入数据库
        payMapper.insert(paymentRecords);
        //3.获取对应支付策略
        BasePayResultVO basePayResultVO = abstractPayStrategyHandler.choosePay(payReqDTO, PaymentTypeEnum.getPaymentTypeByCode(payReqDTO.getPaymentType()));
        return basePayResultVO;
    }

    @Override
    public String payNotify(HttpServletRequest request) {
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            System.out.println("=========支付宝异步回调========");

            Map<String, String> params = new HashMap<>();
            // 返回的所有元素 其中有gmt_create=2024-03-16 22:26:17, charset=utf-8, gmt_payment=2024-03-16 22:26:21, notify_time=2024-03-16 22:26:23, subject=测试商品
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                // servlet写法 通过key获取value
                params.put(name, request.getParameter(name));
            }
            System.out.println(params);
            System.out.println(params.size());
            String orderId = params.get("out_trade_no");
            String paymentTime = params.get("gmt_payment");

            // 支付宝验签
            // 这里必须要初始化不然报错
            if (Factory.Payment.Common().verifyNotify(params)) {
                // 验签通过
                System.out.println("交易名称: " + params.get("subject"));
                System.out.println("交易状态: " + params.get("trade_status"));
                System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
                System.out.println("商户订单号: " + params.get("out_trade_no"));
                System.out.println("交易金额: " + params.get("total_amount"));
                System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
                System.out.println("买家付款时间: " + params.get("gmt_payment"));
                System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));
                // 更新订单未已支付
                // 做一些业务上的处理 例如说支付成功以后 更新订单状态 改为已支付等等
                Orders order = new Orders();


                order.setOrderNo(orderId);

                order.setStatus(1);

                LocalDateTime dateTime = LocalDateTime.parse(paymentTime);
                order.setPaymentTime(dateTime);

                orderMapper.updatePayOrder(order);
            }
        }
        return "success";
    }
    @Override
    public Orders getNewOrder(String orderNo){
        Orders order = orderMapper.queryByOrderNo(orderNo);
        return order;
    }
}
