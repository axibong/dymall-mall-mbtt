package cn.mbtt.service.domain.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 支付记录实体类，用于映射数据库中的 payment_records 表。
 * <p>
 * 该类包含了支付记录的基本信息、订单信息、支付状态以及时间戳字段。
 * </p>
 *
 * @author axi
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 支付记录ID，唯一标识支付记录的自增主键。
     */
    private Long id;

    /**
     * 订单ID，关联的订单ID，指向订单表的id字段。
     */
    private Long orderId;

    /**
     * 支付单号，支付的唯一单号。
     */
    private String paymentNo;

    /**
     * 交易ID，支付平台的交易ID。
     */
    private String transactionId;

    /**
     * 支付金额，支付的金额。
     */
    private Double amount;

    /**
     * 支付方式：
     * <ul>
     *     <li>1 - 支付宝</li>
     *     <li>2 - 微信</li>
     *     <li>3 - 信用卡</li>
     * </ul>
     */
    private Integer paymentType;

    /**
     * 支付状态：
     * <ul>
     *     <li>0 - 待支付</li>
     *     <li>1 - 支付成功</li>
     *     <li>2 - 支付失败</li>
     *     <li>3 - 已退款</li>
     * </ul>
     */
    private Integer status;

    /**
     * 回调时间，支付平台的回调时间。
     */
    private LocalDateTime callbackTime;

    /**
     * 回调数据，支付平台返回的完整回调数据。
     */
    private String callbackData;

    /**
     * 记录创建时间，默认为当前时间。
     */
    private LocalDateTime createdAt;

    /**
     * 记录更新时间，默认为当前时间，并在更新时自动刷新。
     */
    private LocalDateTime updatedAt;

    /**
     * 索引设计，加快按订单ID查询支付记录的速度。
     */
    public static final String INDEX_ORDER_ID = "idx_order_id";

    /**
     * 索引设计，加快按支付单号查询支付记录的速度。
     */
    public static final String INDEX_PAYMENT_NO = "idx_payment_no";

    /**
     * 索引设计，加快按交易ID查询支付记录的速度。
     */
    public static final String INDEX_TRANSACTION_ID = "idx_transaction_id";

    /**
     * 索引设计，加快按创建时间查询支付记录的速度。
     */
    public static final String INDEX_CREATED_AT = "idx_created_at";
}