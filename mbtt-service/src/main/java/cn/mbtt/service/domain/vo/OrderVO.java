package cn.mbtt.service.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(description = "订单展示对象")
public class OrderVO {

    @ApiModelProperty("订单ID")
    private Long id;

    @ApiModelProperty("总金额，单位为分")
    private BigDecimal totalFee;

    @ApiModelProperty("支付类型，1、支付宝，2、微信，3、扣减余额")
    private Integer paymentType;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("订单的状态，1、未付款 2、已付款,未发货 3、已发货,未确认 4、确认收货，交易成功 5、交易取消，订单关闭 6、交易结束，已评价")
    private Integer status;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("支付时间")
    private LocalDateTime payTime;

    @ApiModelProperty("发货时间")
    private LocalDateTime consignTime;

    @ApiModelProperty("交易完成时间")
    private LocalDateTime endTime;

    @ApiModelProperty("交易关闭时间")
    private LocalDateTime closeTime;

    @ApiModelProperty("评价时间")
    private LocalDateTime commentTime;

    @ApiModelProperty("订单商品项列表")
    private List<OrderItemVO> items;

    @Data
    @ApiModel(description = "订单商品项展示对象")
    public static class OrderItemVO {

        @ApiModelProperty("商品ID")
        private Long productId;

        @ApiModelProperty("商品名称")
        private String productName;

        @ApiModelProperty("商品图片")
        private String productImage;

        @ApiModelProperty("商品单价")
        private BigDecimal price;

        @ApiModelProperty("商品数量")
        private Integer quantity;

        @ApiModelProperty("商品总价")
        private BigDecimal totalPrice;
    }
}