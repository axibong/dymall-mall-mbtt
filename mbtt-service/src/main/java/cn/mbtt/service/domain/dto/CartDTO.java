
package cn.mbtt.service.domain.dto;

import java.util.List;

public class CartDTO {

    private Long userId;
    private List<CartItemDTO> items;  // 购物车中的商品列表


    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }

}
