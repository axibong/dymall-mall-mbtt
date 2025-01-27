package cn.mbtt.service.domain.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品表实体类，用于映射数据库中的 products 表。
 * <p>
 * 该类包含了商品的基本信息、分类信息、价格、库存、销售数量、状态信息以及时间戳字段。
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
public class Products implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID，唯一标识商品的自增主键。
     */
    private Long id;

    /**
     * 商品所属分类ID，指向分类表的id字段。
     */
    private Long categoryId;

    /**
     * 商品名称，最大长度为100字符。
     */
    private String name;

    /**
     * 商品的详细描述，可包含HTML等格式。
     */
    private String description;

    /**
     * 商品的销售价格，保留两位小数。
     */
    private BigDecimal price;

    /**
     * 商品的原始价格，保留两位小数（可为空）。
     */
    private BigDecimal originalPrice;

    /**
     * 商品库存数量，默认为0。
     */
    private Integer stock;

    /**
     * 商品图片信息，存储为JSON格式，可包含多个图片链接。
     */
    private List<String> images;

    /**
     * 商品的销售数量，默认为0。
     */
    private Integer salesCount;

    /**
     * 商品状态：
     * <ul>
     *     <li>1 - 在售</li>
     *     <li>0 - 下架</li>
     *     <li>-1 - 删除</li>
     * </ul>
     */
    private Integer status;

    /**
     * 商品记录创建时间，默认为当前时间。
     */
    private LocalDateTime createdAt;

    /**
     * 商品记录最近更新时间，默认为当前时间，并在更新时自动刷新。
     */
    private LocalDateTime updatedAt;

    /**
     * 商品删除时间，若为NULL表示未删除（软删除字段）。
     */
    private LocalDateTime deletedAt;
}