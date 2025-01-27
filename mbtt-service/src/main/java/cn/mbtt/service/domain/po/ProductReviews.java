package cn.mbtt.service.domain.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 产品评论表实体类，用于映射数据库中的 product_reviews 表。
 * <p>
 * 该类包含了产品评论的基本信息、用户ID、商品ID、订单ID、评分、内容、图片、状态信息以及时间戳字段。
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
public class ProductReviews implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论ID，唯一标识产品评论的自增主键。
     */
    private Long id;

    /**
     * 评论的用户ID，指向用户表的id字段。
     */
    private Long userId;

    /**
     * 评论所属商品的ID，指向商品表的id字段。
     */
    private Long productId;

    /**
     * 关联的订单ID，指向订单表的id字段。
     */
    private Long orderId;

    /**
     * 评分，取值范围为1到5。
     */
    private Integer rating;

    /**
     * 评论的文字内容。
     */
    private String content;

    /**
     * 评论的附带图片，存储图片的URL或路径信息。
     */
    private List<String> images;

    /**
     * 评论的状态：
     * <ul>
     *     <li>1 - 显示</li>
     *     <li>0 - 隐藏</li>
     * </ul>
     */
    private Integer status;

    /**
     * 评论的创建时间，默认为当前时间。
     */
    private LocalDateTime createdAt;

    /**
     * 评论的更新时间，默认为当前时间，并在更新时自动刷新。
     */
    private LocalDateTime updatedAt;

    /**
     * 评论的删除时间，若为NULL表示未删除。
     */
    private LocalDateTime deletedAt;
}