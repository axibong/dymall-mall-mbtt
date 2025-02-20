package cn.mbtt.service.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrdersSubmitDTO {
    private Long addressBookId;  // 用户的收货地址ID
    private Long userId;         // 用户ID
    private BigDecimal totalAmount;  // 订单总金额
    private Integer paymentType; // 支付方式（如支付宝，微信等）
}
