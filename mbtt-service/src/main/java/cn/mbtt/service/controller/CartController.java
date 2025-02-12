package cn.mbtt.service.controller;

import cn.mbtt.service.domain.dto.CartFormDTO;
import cn.mbtt.service.domain.vo.CartVO;
import cn.mbtt.service.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 用于处理购物车相关的 HTTP 请求
 */

@Api(tags = "购物车相关接口")
@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    /**
     * 获取所有购物车中的商品
     * @return 购物车商品列表
     */
    @GetMapping("/items")
    @ApiOperation(value = "获取购物车商品", notes = "查询当前购物车中的所有商品")
    public List<CartVO> getCartItems() {
        return cartService.getCartItems();
    }

    /**
     * 向购物车中添加商品
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加商品到购物车", notes = "根据商品 ID 和数量，将商品加入购物车")
    public void addItemToCart(@Valid @RequestBody CartFormDTO cartFormDTO) {
        cartService.addItemToCart(cartFormDTO);
    }
    /**
     * 从购物车中移除商品
     * @param productId 商品ID
     */
    @DeleteMapping("/remove")
    @ApiOperation(value = "移除购物车商品", notes = "根据商品 ID 将商品从购物车中移除")
    public void removeItemFromCart(
            @ApiParam(value = "商品 ID", required = true) @RequestParam Long productId) {
        cartService.removeItemFromCart(productId);
    }
}
