package cn.mbtt.service.mapper;

import cn.mbtt.service.domain.po.OrderItems;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrderItemMapper {
    @Select("<script>" +
            "SELECT * FROM order_items " +
            "WHERE order_id IN " +
            "<foreach item='orderId' collection='orderIds' open='(' separator=',' close=')'>" +
            "#{orderId}" +
            "</foreach>" +
            "</script>")
    List<OrderItems> selectByOrderIds(@Param("orderIds") List<Long> orderIds);


    /**
     * 根据用户ID查询所有与用户相关的订单项
     * @param userId 用户ID
     * @return 返回与该用户相关的所有订单项
     */
    @Select("SELECT oi.* " +
            "FROM order_items oi " +
            "JOIN orders o ON oi.order_id = o.id " +
            "WHERE o.user_id = #{userId}")
    List<OrderItems> selectByOrderId(Long userId);

    /**
     * 批量插入订单商品项
     * @param orderItemsList 订单商品项列表
     */
    @Insert({
            "<script>",
            "INSERT INTO order_items (order_id, product_id, product_snapshot, quantity, price, created_at) ",
            "VALUES ",
            "<foreach collection='orderItemsList' item='item' index='index' separator=','>",
            "(#{item.orderId}, #{item.productId}, #{item.productSnapshot}, #{item.quantity}, #{item.price}, #{item.createdAt})",
            "</foreach>",
            "</script>"
    })
    void insertOrderItems(List<OrderItems> orderItemsList);
}