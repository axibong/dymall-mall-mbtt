package cn.mbtt.service.mapper;

import cn.mbtt.service.domain.po.OrderItems;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    @SelectProvider(type = OrderItemSqlProvider.class, method = "selectByOrderIds")
    List<OrderItems> selectByOrderIds(@Param("orderIds") List<Long> orderIds);

    @Select("SELECT oi.* FROM order_items oi JOIN orders o ON oi.order_id = o.id WHERE o.user_id = #{userId}")
    List<OrderItems> selectByOrderId(Long userId);

    @InsertProvider(type = OrderItemSqlProvider.class, method = "insertOrderItems")
    void insertOrderItems(@Param("orderItemsList") List<OrderItems> orderItemsList);

    // SQL Provider 类
    class OrderItemSqlProvider {
        public String selectByOrderIds(@Param("orderIds") List<Long> orderIds) {
            StringBuilder sql = new StringBuilder("SELECT * FROM order_items WHERE order_id IN (");
            for (int i = 0; i < orderIds.size(); i++) {
                sql.append("#{orderIds[").append(i).append("]}");
                if (i < orderIds.size() - 1) {
                    sql.append(",");
                }
            }
            sql.append(")");
            return sql.toString();
        }

        public String insertOrderItems(@Param("orderItemsList") List<OrderItems> orderItemsList) {
            StringBuilder sql = new StringBuilder(
                    "INSERT INTO order_items (order_id, product_id, product_snapshot, quantity, price, created_at) VALUES "
            );
            for (int i = 0; i < orderItemsList.size(); i++) {
                sql.append("(")
                        .append("#{orderItemsList[").append(i).append("].orderId},")
                        .append("#{orderItemsList[").append(i).append("].productId},")
                        .append("#{orderItemsList[").append(i).append("].productSnapshot},")
                        .append("#{orderItemsList[").append(i).append("].quantity},")
                        .append("#{orderItemsList[").append(i).append("].price},")
                        .append("#{orderItemsList[").append(i).append("].createdAt}")
                        .append(")");
                if (i < orderItemsList.size() - 1) {
                    sql.append(",");
                }
            }
            return sql.toString();
        }
    }
}