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
 * 订单商品项表实体类，用于映射数据库中的 order_items 表。
 * <p>
 * 该类包含了订单商品项的基本信息、订单ID、商品ID、商品快照、数量、价格以及创建时间字段。
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
public class OrderItems implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单商品项ID，唯一标识订单商品项的自增主键。
     */
    private Long id;

    /**
     * 所属订单的ID，指向订单表的id字段。
     */
    private Long orderId;

    /**
     * 商品ID，指向商品表的id字段。
     */
    private Long productId;

    /**
     * 下单时商品的快照，包含商品的相关信息（如名称、价格、描述等）。
     */
    private Map<String, Object> productSnapshot;

    /**
     * 商品购买数量。
     */
    private Integer quantity;

    /**
     * 商品单价。
     */
    private BigDecimal price;

    /**
     * 商品项创建时间，默认为当前时间。
     */
    private LocalDateTime createdAt;
}