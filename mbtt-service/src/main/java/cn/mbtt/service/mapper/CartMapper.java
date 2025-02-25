package cn.mbtt.service.mapper;

import cn.mbtt.service.domain.dto.CartDTO;
import cn.mbtt.service.domain.po.ShoppingCartItems;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartMapper {

    // 查询用户购物车中的所有商品
    @Select("SELECT * FROM shopping_cart_items WHERE user_id = #{userId}")
    List<ShoppingCartItems> findItemsByUserId(Long userId);

    // 根据用户ID和商品ID删除购物车中的商品
    @Delete("DELETE FROM shopping_cart_items WHERE user_id = #{userId} AND product_id = #{productId}")
    void deleteByProductIdAndUserId(@Param("productId") Long productId, @Param("userId") Long userId);

    // 查询购物车中商品的数量
    @Select("SELECT COUNT(*) FROM shopping_cart_items WHERE user_id = #{userId}")
    int countItemsInCart2(Long userId);

    // 查询购物车中是否已存在该商品
    @Select("SELECT COUNT(*) FROM shopping_cart_items WHERE product_id = #{productId} AND user_id = #{userId}")
    int countItemsInCart(@Param("productId") Long productId, @Param("userId") Long userId);

    // 插入商品到购物车
    @Insert("INSERT INTO shopping_cart_items (product_id, user_id, quantity) VALUES (#{productId}, #{userId}, #{quantity})")
    void insert(ShoppingCartItems cart);

    // 更新购物车中商品的数量
    @Update("UPDATE shopping_cart_items SET quantity = #{quantity} WHERE product_id = #{productId} AND user_id = #{userId}")
    void updateNum(@Param("productId") Long productId, @Param("userId") Long userId, @Param("quantity") Integer quantity);

    @Update({
            "<script>",
            "    <foreach collection='cartDTO.items' item='item' index='index' open='' separator=';' close=''>",
            "        INSERT INTO shopping_cart_items (product_id, user_id, quantity, product_name, price) " +
                    "           VALUES ( #{item.productId},#{cartDTO.userId}, #{item.quantity}, #{item.productName}, #{item.price})",
            "        ON DUPLICATE KEY UPDATE " +
                    "           quantity = #{item.quantity}, " +
                    "    </foreach>",
            "</script>"
    })
    void updateCart(@Param("cartDTO") CartDTO cartDTO);

}
