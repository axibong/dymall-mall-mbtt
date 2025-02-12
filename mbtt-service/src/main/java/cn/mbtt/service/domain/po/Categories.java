package cn.mbtt.service.domain.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品分类表实体类，用于映射数据库中的 categories 表。
 * <p>
 * 该类包含了商品分类的基本信息、层级关系、排序字段、状态信息以及时间戳字段。
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
public class Categories implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类ID，唯一标识分类的自增主键。
     */
    private Long id;

    /**
     * 分类名称，最大长度为50字符。
     */
    private String name;

    /**
     * 上级分类ID，指向本表的id字段，NULL表示顶级分类。
     */
    private Long parentId;

    /**
     * 分类层级，用于表示当前分类所在的深度，从1开始。
     */
    private Integer level;

    /**
     * 分类排序字段，数值越小优先级越高。
     */
    private Integer sortOrder;

    /**
     * 分类状态：
     * <ul>
     *     <li>1 - 活跃</li>
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

    /**
     * 记录删除时间，若为NULL表示未删除（软删除字段）。
     */
    private LocalDateTime deletedAt;
}