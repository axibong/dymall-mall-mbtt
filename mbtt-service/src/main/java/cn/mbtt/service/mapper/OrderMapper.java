package cn.mbtt.service.mapper;

import cn.mbtt.service.domain.dto.OrdersPageQueryDTO;
import cn.mbtt.service.domain.po.Orders;
import com.github.pagehelper.Page;

public interface OrderMapper {


    Page<Orders> queryOrders(OrdersPageQueryDTO dto);

    void insertOrders(Orders order);
}
