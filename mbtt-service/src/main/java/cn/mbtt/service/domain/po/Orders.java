package cn.mbtt.service.domain.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 订单表实体类，用于映射数据库中的 orders 表。
 * <p>
 * 该类包含了订单的基本信息、用户ID、金额信息、地址快照、状态信息、支付信息以及时间戳字段。
 * </p>
 *
 * @author axi
 * @version 1.0
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID，唯一标识订单的自增主键。
     */
    private Long id;

    /**
     * 订单唯一编号。
     */
    private String orderNo;

    /**
     * 下单用户的ID，指向用户表的id字段。
     */
    private Long userId;

    /**
     * 订单总金额。
     */
    private BigDecimal totalAmount;

    /**
     * 用户实际支付金额（可能包含优惠、折扣等）。
     */
    private BigDecimal actualAmount;

    /**
     * 下单时的地址快照，包含收货信息。
     */
    private Map<String, Object> addressSnapshot;

    /**
     * 订单状态：
     * <ul>
     *     <li>0 - 待支付</li>
     *     <li>1 - 已支付</li>
     *     <li>2 - 已发货</li>
     *     <li>3 - 已送达</li>
     *     <li>4 - 已完成</li>
     *     <li>-1 - 已取消</li>
     * </ul>
     */
    private Integer status;

    /**
     * 支付方式：
     * <ul>
     *     <li>1 - 支付宝</li>
     *     <li>2 - 微信支付</li>
     *     <li>3 - 信用卡支付</li>
     * </ul>
     */
    private Integer paymentType;

    /**
     * 支付时间（若已支付）。
     */
    private LocalDateTime paymentTime;

    /**
     * 发货时间（若已发货）。
     */
    private LocalDateTime shippingTime;

    /**
     * 送达时间（若已送达）。
     */
    private LocalDateTime deliveryTime;

    /**
     * 订单完成时间（若已完成）。
     */
    private LocalDateTime completionTime;

    /**
     * 订单取消时间（若已取消）。
     */
    private LocalDateTime cancelTime;

    /**
     * 订单取消原因（若已取消）。
     */
    private String cancelReason;

    /**
     * 订单创建时间，默认为当前时间。
     */
    private LocalDateTime createdAt;

    /**
     * 订单最近更新时间，默认为当前时间，并在更新时自动刷新。
     */
    private LocalDateTime updatedAt;
}