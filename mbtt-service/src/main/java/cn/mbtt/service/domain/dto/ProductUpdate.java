package cn.mbtt.service.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductUpdate {
    private Long categoryId;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer stock;
    private List<String> images;
    private Integer status;
    private Integer salesCount;
    private LocalDateTime updatedAt;


}
