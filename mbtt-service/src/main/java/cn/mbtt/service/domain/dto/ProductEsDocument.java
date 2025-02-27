package cn.mbtt.service.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.annotation.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter

@Document(indexName = "products")
public class ProductEsDocument {

    @Id
    private Long id;

    private String name;
    private String description;
    private Long categoryId;
    private Integer status;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer stock;
    private List<String> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
