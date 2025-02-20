package cn.mbtt.service.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单展示对象
 */
@Data
@ApiModel(description = "订单展示对象")
public class OrderVO {

    @ApiModelProperty("订单ID")
    private Long id;

    @ApiModelProperty("订单唯一编号")
    private String orderNo;  // 添加订单编号，通常需要用于支付等场景

    @ApiModelProperty("总金额，单位为分")
    private BigDecimal totalFee;

    @ApiModelProperty("支付类型，1、支付宝，2、微信，3、扣减余额")
    private Integer paymentType;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("订单的状态")
    private Integer status;

    @ApiModelProperty("订单的状态描述，便于展示状态的中文")
    private String statusDescription;  // 添加状态描述字段，便于显示

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

        @ApiModelProperty("商品单价，单位：分")
        private BigDecimal price;

        @ApiModelProperty("商品数量")
        private Integer quantity;

        @ApiModelProperty("商品总价，单位：分")
        private BigDecimal totalPrice;

        @ApiModelProperty("商品规格或配料信息")
        private String productSpecifications;
    }
}
