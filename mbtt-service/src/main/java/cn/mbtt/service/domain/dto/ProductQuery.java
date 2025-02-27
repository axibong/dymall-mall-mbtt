package cn.mbtt.service.domain.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductQuery {
    // 精确查询字段
    private Long id;
    private Long categoryId;
    private Integer status;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer salesCount;

    // 模糊查询字段
    private String name;         // 商品名称（模糊查询）
    private String description;  // 商品描述（模糊查询）

    // 范围查询字段
    private BigDecimal minPrice;       // 最低价格
    private BigDecimal maxPrice;       // 最高价格
    private Integer minStock;         // 最小库存
    private Integer maxStock;         // 最大库存
    private Integer minSalesCount;    // 最小销量
    private Integer maxSalesCount;    // 最大销量
    private LocalDateTime startCreatedAt;  // 创建时间起始范围
    private LocalDateTime endCreatedAt;    // 创建时间结束范围
    private LocalDateTime startUpdatedAt;  // 更新时间起始范围
    private LocalDateTime endUpdatedAt;    // 更新时间结束范围

    // 新增排序字段
    private String sortField;     // 排序字段（如 price, createdAt 等）
    private String sortOrder;     // 排序方式（asc/desc）
}