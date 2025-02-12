package cn.mbtt.service.domain.dto;

/**
 * 用于在服务层与控制器之间传递购物车的数据。
 */
public class CartDTO {

    private Long productId;  // 商品ID
    private Integer quantity;  // 商品数量

    public CartDTO() {
    }

    public CartDTO(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getter 和 Setter 方法
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartDTO{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
