package cn.mbtt.service.service;

import cn.mbtt.service.domain.dto.CartFormDTO;
import cn.mbtt.service.domain.vo.CartVO;

import javax.validation.Valid;
import java.util.List;

public interface CartService {

    List<CartVO> getCartItems();

    void removeItemFromCart(Long productId);

    void addItemToCart(@Valid CartFormDTO cartFormDTO);
}
