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


    @Update("UPDATE orders SET status = -1, cancel_time = NOW(), cancel_reason = #{cancelReason} WHERE id = #{orderId}")
    void update(Orders orders);
}
