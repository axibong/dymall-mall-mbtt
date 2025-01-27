package cn.mbtt.service.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(description = "购物车展示对象")
public class CartVO {

    @ApiModelProperty("购物车ID")
    private Long id;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("商品列表")
    private List<CartItemVO> items;

    @ApiModelProperty("总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("是否全选")
    private Boolean selectedAll;

    @Data
    @ApiModel(description = "购物车商品项展示对象")
    public static class CartItemVO {

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

        @ApiModelProperty("是否选中")
        private Boolean selected;

        @ApiModelProperty("商品总价")
        private BigDecimal totalPrice;
    }
}