//package cn.mbtt.service.service.impl;
//
//import cn.mbtt.common.exception.BizIllegalException;
//import cn.mbtt.common.utils.BeanUtils;
//import cn.mbtt.service.domain.dto.CartFormDTO;
//import cn.mbtt.service.domain.po.ShoppingCartItems;
//import cn.mbtt.service.domain.po.Users;
//import cn.mbtt.service.domain.vo.CartVO;
//import cn.mbtt.service.mapper.CartMapper;
//import cn.mbtt.service.service.CartService;
//import cn.mbtt.service.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import java.util.List;
//import java.util.ArrayList;
//
//@Service
//public class CartServiceImpl implements CartService {
//
//    @Autowired
//    private CartMapper cartMapper;
//    @Autowired
//    private UserService userService;
//
//    /**
//     * 向购物车中添加商品
//     * @param cartFormDTO 商品表单DTO
//     */
//    @Override
//    public void addItemToCart(CartFormDTO cartFormDTO) {
//        // 1. 获取当前登录用户
//        Users currentUser = userService.getCurrentUser();
//
//        // 2. 检查商品是否已经存在于购物车
//        if (checkItemExists(cartFormDTO.getProductId(), currentUser.getId())) {
//            // 商品已存在，更新商品数量
//            cartMapper.updateNum(cartFormDTO.getProductId(), currentUser.getId(), cartFormDTO.getQuantity());
//            return;
//        }
//
//        // 3. 创建一个 PO 对象并将 DTO 转换为 PO
//        ShoppingCartItems cart = new ShoppingCartItems();
//        BeanUtils.copyProperties(cartFormDTO, cart);  // 将 DTO 属性复制到 PO 对象
//        cart.setUserId(currentUser.getId());  // 设置当前用户 ID
//
//        // 5. 保存商品到数据库
//        cartMapper.insert(cart);
//    }
//
//    /**
//     * 检查商品是否已经存在于购物车
//     * @param productId 商品 ID
//     * @param userId 用户 ID
//     * @return 是否存在
//     */
//    private boolean checkItemExists(Long productId, Long userId) {
//        int count = cartMapper.countItemsInCart(productId, userId);
//        return count > 0;
//    }
//
//    /**
//     * 获取当前用户的购物车商品
//     * @return 购物车商品列表
//     */
//    @Override
//    public List<CartVO> getCartItems() {
//        // 1. 获取当前登录用户
//        Users currentUser = userService.getCurrentUser();
//
//        // 2. 查询购物车商品
//        List<ShoppingCartItems> cartItems = cartMapper.findItemsByUserId(currentUser.getId());
//
//        // 3. 将 PO 转换为 VO
//        List<CartVO> cartVOList = new ArrayList<>();
//        for (ShoppingCartItems item : cartItems) {
//            CartVO cartVO = new CartVO();
//            BeanUtils.copyProperties(item, cartVO);
//            cartVOList.add(cartVO);
//        }
//
//        return cartVOList;
//    }
//
//    /**
//     * 移除购物车中的商品
//     * @param productId 商品 ID
//     */
//    @Override
//    public void removeItemFromCart(Long productId) {
//        // 1. 获取当前登录用户
//        Users currentUser = userService.getCurrentUser();
//
//        // 2. 从购物车中移除商品
//        cartMapper.deleteByProductIdAndUserId(productId, currentUser.getId());
//    }
//
//}
