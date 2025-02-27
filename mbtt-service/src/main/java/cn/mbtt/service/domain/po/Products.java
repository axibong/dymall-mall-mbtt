package cn.mbtt.service.domain.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.DateFormat;

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
@Document(indexName = "products")
public class Products implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID，唯一标识商品的自增主键。
     */
    @Field(type= FieldType.Keyword)
    private Long id;

    /**
     * 商品所属分类ID，指向分类表的id字段。
     */
    @Field(type = FieldType.Keyword)
    private Long categoryId;

    /**
     * 商品名称，最大长度为100字符。
     */
    @Field(type = FieldType.Text)
    private String name;

    /**
     * 商品的详细描述，可包含HTML等格式。
     */
    @Field(type = FieldType.Text)
    private String description;

    /**
     * 商品的销售价格，保留两位小数。
     */
    @Field(type=FieldType.Scaled_Float,scalingFactor = 100)
    private BigDecimal price;

    /**
     * 商品的原始价格，保留两位小数（可为空）。
     */
    @Field(type =FieldType.Scaled_Float,scalingFactor = 100)
    private BigDecimal originalPrice;

    /**
     * 商品库存数量，默认为0。
     */
    @Field(type=FieldType.Integer)
    private Integer stock;

    /**
     * 商品图片信息，存储为JSON格式，可包含多个图片链接。
     */
    @Field(type = FieldType.Keyword)
    private List<String> images;

    /**
     * 商品的销售数量，默认为0。
     */
    @Field(type = FieldType.Integer)

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
    @Field(type = FieldType.Date, format = {DateFormat.date_hour_minute_second, DateFormat.date})
    private LocalDateTime createdAt;

    @Field(type = FieldType.Date, format = {DateFormat.date_hour_minute_second, DateFormat.date})
    private LocalDateTime updatedAt;

    @Field(type = FieldType.Date, format = {DateFormat.date_hour_minute_second, DateFormat.date})
    private LocalDateTime deletedAt;

}
