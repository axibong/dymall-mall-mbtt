package cn.mbtt.service.domain.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 购物车商品项表实体类，用于映射数据库中的 shopping_cart_items 表。
 * <p>
 * 该类包含了购物车商品项的基本信息、用户ID、商品ID、数量、选中状态、状态信息以及时间戳字段。
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
public class ShoppingCartItems implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 购物车商品项ID，唯一标识购物车商品项的自增主键。
     */
    private Long id;

    /**
     * 购物车所属用户的ID，指向用户表的id字段。
     */
    private Long userId;

    /**
     * 购物车中商品的ID，指向商品表的id字段。
     */
    private Long productId;

    /**
     * 购物车中该商品的数量，默认为1。
     */
    private Integer quantity;

    /**
     * 是否选中该商品，默认为选中。
     */
    private Boolean selected;

    /**
     * 商品项状态：
     * <ul>
     *     <li>1 - 有效</li>
     *     <li>0 - 无效</li>
     * </ul>
     */
    private Integer status;

    /**
     * 记录创建时间，默认为当前时间。
     */
    private LocalDateTime createdAt;

    /**
     * 记录最近更新时间，默认为当前时间，并在更新时自动刷新。
     */
    private LocalDateTime updatedAt;
}