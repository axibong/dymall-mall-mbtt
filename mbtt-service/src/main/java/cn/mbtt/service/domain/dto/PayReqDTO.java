package cn.mbtt.service.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 支付请求
 * */
@Data
@ApiModel(description = "支付")
public class PayReqDTO
{
    /**
     * 订单ID
     * */
    @NotNull(message = "orderId不能为空")
    private Long orderId;
    /**
     * 金额
     * */
    private Double amount;
    /**
     * 支付方式，1：支付宝，2：微信，3：信用卡。
     * @reference cn.mbtt.service.enums.PaymentTypeEnum
     * */
    @NotNull(message = "支付方式不能为空")
    private Integer paymentType;


}
