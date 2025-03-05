package cn.mbtt.service.service.impl;

import cn.mbtt.service.domain.dto.PayReqDTO;
import cn.mbtt.service.domain.po.PaymentRecords;
import cn.mbtt.service.enums.PayStatusEnum;
import cn.mbtt.service.enums.PaymentTypeEnum;
import cn.mbtt.service.handler.AbstractPayStrategyHandler;
import cn.mbtt.service.mapper.PayMapper;
import cn.mbtt.service.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PayServiceImpl implements PayService {

    @Autowired
    private PayMapper payMapper;
    @Autowired
    private List<AbstractPayStrategyHandler> handlers; // 注入所有策略实现

    @Override
    public void pay(PayReqDTO payReqDTO) {
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
        // 动态选择策略
        String paymentType = PaymentTypeEnum.getPaymentTypeByCode(payReqDTO.getPaymentType());
        AbstractPayStrategyHandler handler = handlers.stream()
                .filter(h -> h.paymentType().equals(paymentType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported payment type: " + paymentType));
        handler.pay();
    }
}
