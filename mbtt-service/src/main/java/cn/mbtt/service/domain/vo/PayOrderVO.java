package cn.mbtt.service.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(description = "支付订单展示对象")
public class PayOrderVO {

    @ApiModelProperty("支付订单ID")
    private Long id;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("支付金额，单位为分")
    private BigDecimal amount;

    @ApiModelProperty("支付类型，1、支付宝，2、微信，3、扣减余额")
    private Integer paymentType;

    @ApiModelProperty("支付状态，0、待支付 1、支付成功 2、支付失败 3、已退款")
    private Integer status;

    @ApiModelProperty("支付时间")
    private String payTime;

    @ApiModelProperty("支付平台交易号")
    private String transactionId;

    @ApiModelProperty("支付回调数据")
    private String callbackData;
}