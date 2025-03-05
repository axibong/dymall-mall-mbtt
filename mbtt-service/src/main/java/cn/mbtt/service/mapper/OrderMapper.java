package cn.mbtt.service.mapper;

import cn.mbtt.service.domain.dto.OrdersPageQueryDTO;
import cn.mbtt.service.domain.po.Orders;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import cn.mbtt.service.handler.JsonMapTypeHandler;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {
    @SelectProvider(type = OrderSqlProvider.class, method = "queryOrders")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "orderNo", column = "order_no"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "totalAmount", column = "total_amount"),
            @Result(property = "actualAmount", column = "actual_amount"),
            @Result(property = "addressSnapshot", column = "address_snapshot", typeHandler = JsonMapTypeHandler.class),
            @Result(property = "status", column = "status"),
            @Result(property = "paymentType", column = "payment_type"),
            @Result(property = "paymentTime", column = "payment_time"),
            @Result(property = "shippingTime", column = "shipping_time"),
            @Result(property = "deliveryTime", column = "delivery_time"),
            @Result(property = "completionTime", column = "completion_time"),
            @Result(property = "cancelTime", column = "cancel_time"),
            @Result(property = "cancelReason", column = "cancel_reason"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at")
    })
    List<Orders> queryOrders(OrdersPageQueryDTO dto);

    @Insert("INSERT INTO orders (order_no, user_id, total_amount, actual_amount, address_snapshot, status, " +
            "payment_type, payment_time, shipping_time, delivery_time, completion_time, cancel_time, " +
            "cancel_reason, created_at, updated_at) " +
            "VALUES (#{orderNo}, #{userId}, #{totalAmount}, #{actualAmount}, " +
            "#{addressSnapshot, typeHandler=cn.mbtt.service.handler.JsonMapTypeHandler}, #{status}, " +
            "#{paymentType}, #{paymentTime}, #{shippingTime}, #{deliveryTime}, #{completionTime}, " +
            "#{cancelTime}, #{cancelReason}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertOrders(Orders order);

    @Select("SELECT * FROM orders WHERE id = #{orderId} AND status = 0 AND delete_status = 0")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "orderNo", column = "order_no"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "totalAmount", column = "total_amount"),
            @Result(property = "actualAmount", column = "actual_amount"),
            @Result(property = "addressSnapshot", column = "address_snapshot", typeHandler = JsonMapTypeHandler.class),
            @Result(property = "status", column = "status"),
            @Result(property = "paymentType", column = "payment_type"),
            @Result(property = "paymentTime", column = "payment_time"),
            @Result(property = "shippingTime", column = "shipping_time"),
            @Result(property = "deliveryTime", column = "delivery_time"),
            @Result(property = "completionTime", column = "completion_time"),
            @Result(property = "cancelTime", column = "cancel_time"),
            @Result(property = "cancelReason", column = "cancel_reason"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at")
    })
    Orders queryById(Long orderId);

    @Update("UPDATE orders SET status = -1, cancel_time = NOW(), cancel_reason = #{cancelReason} WHERE id = #{id}")
    void update(Orders orders);

    // SQL Provider 类，用于动态生成 queryOrders 的 SQL
    class OrderSqlProvider {
        public String queryOrders(OrdersPageQueryDTO dto) {
            StringBuilder sql = new StringBuilder(
                    "SELECT id, order_no, user_id, total_amount, actual_amount, address_snapshot, status, " +
                            "payment_type, payment_time, shipping_time, delivery_time, completion_time, " +
                            "cancel_time, cancel_reason, created_at, updated_at FROM orders WHERE 1=1"
            );

            if (dto.getStatus() != null) {
                sql.append(" AND status = #{orderStatus}");
            }
            if (dto.getPaymentType() != null) {
                sql.append(" AND payment_type = #{paymentType}");
            }
            if (dto.getOrderNo() != null && !dto.getOrderNo().isEmpty()) {
                sql.append(" AND order_no LIKE CONCAT('%', #{orderNo}, '%')");
            }
            if (dto.getUserId() != null) {
                sql.append(" AND user_id = #{userId}");
            }
            if (dto.getStartDate() != null) {
                sql.append(" AND created_at >= #{startDate}");
            }
            if (dto.getEndDate() != null) {
                sql.append(" AND created_at <= #{endDate}");
            }
            sql.append(" ORDER BY created_at DESC");

            return sql.toString();
        }
    }
}