package cn.mbtt.service.service;

import cn.mbtt.common.result.PageResult;
import cn.mbtt.service.domain.dto.OrdersPageQueryDTO;
import cn.mbtt.service.domain.dto.OrdersSubmitDTO;
import cn.mbtt.service.domain.vo.OrderVO;

import java.util.List;

public interface OrderService {
    PageResult<OrderVO> queryOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderVO createOrder(OrdersSubmitDTO ordersSubmitDTO);

    void cancelOrder(Long orderId);
}
