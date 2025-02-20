package cn.mbtt.service.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "订单分页查询DTO")
public class OrdersPageQueryDTO {

    @ApiModelProperty("当前页码")
    private int pageNum = 1; // 默认第一页

    @ApiModelProperty("每页记录数")
    private int pageSize = 10; // 默认每页10条记录

    @ApiModelProperty("订单状态：1-未付款，2-已付款未发货，3-已发货未确认，4-已完成，5-已取消")
    private Integer orderStatus;

    @ApiModelProperty("支付方式：1-支付宝，2-微信，3-余额支付")
    private Integer paymentType;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("订单创建起始时间")
    private LocalDateTime startDate;

    @ApiModelProperty("订单创建结束时间")
    private LocalDateTime endDate;
}
