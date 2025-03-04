package cn.mbtt.service.mapper;

import cn.mbtt.service.domain.dto.OrdersPageQueryDTO;
import cn.mbtt.service.domain.po.Orders;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface OrderMapper {


    Page<Orders> queryOrders(OrdersPageQueryDTO dto);

    void insertOrders(Orders order);


    @Select("SELECT * FROM orders WHERE id = #{orderId} AND status = 0 AND delete_status = 0;")
    Orders queryById(Long orderId);

    @Select("SELECT * FROM orders WHERE order_no = #{orderNo}")
    Orders queryByOrderNo(String orderNo);

    @Update("UPDATE orders SET status = -1, cancel_time = NOW(), cancel_reason = #{cancelReason} WHERE id = #{orderId}")
    void update(Orders orders);

    //更新支付成功后的订单信息
    @Update("UPDATE order SET order_no= #{orderNo}, status = #{status}, payment_time = #{paymentTime}")
    void updatePayOrder(Orders order);
}
