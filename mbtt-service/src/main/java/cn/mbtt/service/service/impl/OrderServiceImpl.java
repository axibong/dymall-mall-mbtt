package cn.mbtt.service.service.impl;

import cn.mbtt.common.exception.OrderBusinessException;
import cn.mbtt.common.result.PageResult;
import cn.mbtt.common.utils.BeanUtils;
import cn.mbtt.service.build.OrderItemBuilder;
import cn.mbtt.service.domain.dto.OrdersPageQueryDTO;
import cn.mbtt.service.domain.dto.OrdersSubmitDTO;
import cn.mbtt.service.domain.po.OrderItems;
import cn.mbtt.service.domain.po.Orders;
import cn.mbtt.service.domain.po.Users;
import cn.mbtt.service.domain.vo.OrderVO;
import cn.mbtt.service.mapper.OrderItemMapper;
import cn.mbtt.service.mapper.OrderMapper;
import cn.mbtt.service.mapper.UserMapper;
import cn.mbtt.service.service.OrderService;
import cn.mbtt.service.service.UserService;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper; // 新增依赖
    @Autowired
    private UserMapper userMapper;

    @Override
    public PageResult<OrderVO> queryOrders(OrdersPageQueryDTO dto) {
        Users currentUser = userService.getCurrentUser();
        dto.setUserId(currentUser.getId());

        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        List<Orders> orders = orderMapper.queryOrders(dto);
        PageInfo<Orders> pageInfo = new PageInfo<>(orders);

        List<Long> orderIds = orders.stream().map(Orders::getId).collect(Collectors.toList());
        List<OrderItems> allOrderItems = orderIds.isEmpty() ? Collections.emptyList() : orderItemMapper.selectByOrderIds(orderIds);
        Map<Long, List<OrderItems>> itemsByOrderId = allOrderItems.stream()
                .collect(Collectors.groupingBy(OrderItems::getOrderId));

        List<OrderVO> voList = orders.stream()
                .map(order -> {
                    OrderVO vo = convertToOrderVO(order);
                    vo.setItems(convertOrderItems(itemsByOrderId.getOrDefault(order.getId(), Collections.emptyList())));
                    return vo;
                })
                .collect(Collectors.toList());

        return new PageResult<>(pageInfo.getTotal(), voList);
    }

    @Transactional
    @Override
    public OrderVO createOrder(OrdersSubmitDTO submitDTO) {
        // 使用构建器模式构建订单对象
        OrderItemBuilder builder = new OrderItemBuilder(submitDTO, orderMapper, orderItemMapper, userMapper);
        builder.builderOrderInfo()
                .builderOrderItems();
        Orders order = builder.build();

        // 将订单转换为 VO 以供前端展示
        return builder.toOrderVO();
    }

    @Transactional
    @Override
    public void cancelOrder(Long orderId) {
        Long userId = userService.getCurrentUser().getId();
        Orders orders = orderMapper.queryById(orderId);
        if (orders == null) {
            throw new OrderBusinessException("订单不存在");
        }
        if (!orders.getUserId().equals(userId)) {
            throw new OrderBusinessException("无权操作该订单");
        }
        if (orders.getStatus() > 0) {
            throw new OrderBusinessException("当前状态不允许取消");
        }
        if (orders.getCreatedAt() != null && LocalDateTime.now().isAfter(orders.getCreatedAt().plusMinutes(30))) {
            throw new OrderBusinessException("已超过可取消时间");
        }

        orders.setStatus(-1);
        orders.setCancelTime(LocalDateTime.now());
        String cancelReason = "用户主动取消"; // 默认值直接赋值
        orders.setCancelReason(cancelReason);

        orderMapper.update(orders);
        afterCancelHandler(orders);
    }

    private void afterCancelHandler(Orders orders) {
//        // 解锁库存：取消订单后，释放商品库存
//        List<OrderItems> orderItems = orderItemMapper.selectByOrderId(orders.getId());
//        for (OrderItems orderItem : orderItems) {
//            int count = stockService.releaseStock(orderItem.getProductSkuId(), orderItem.getQuantity());
//            if (count == 0) {
//                throw new OrderBusinessException("库存不足，无法释放！");
//            }
//        }
    }


    /**
     * 单个 Orders 转换为 OrderVO（基础字段）
     */
    private OrderVO convertToOrderVO(Orders order) {
        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(order, vo);
        vo.setTotalFee(order.getTotalAmount());
        return vo;
    }

    /**
     * 将 OrderItems 列表转换为 OrderItemVO 列表
     */
    private List<OrderVO.OrderItemVO> convertOrderItems(List<OrderItems> items) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }

        return items.stream()
                .map(item -> {
                    OrderVO.OrderItemVO itemVO = new OrderVO.OrderItemVO();
                    itemVO.setProductId(item.getProductId());
                    // 安全处理快照字段
                    Map<String, Object> snapshot = item.getProductSnapshot() != null
                            ? item.getProductSnapshot()
                            : Collections.emptyMap();
                    itemVO.setProductName((String) snapshot.getOrDefault("productName", "未知商品"));
                    itemVO.setProductImage((String) snapshot.getOrDefault("productImage", ""));
                    itemVO.setPrice(item.getPrice());
                    itemVO.setQuantity(item.getQuantity());
                    itemVO.setTotalPrice(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                    return itemVO;
                })
                .collect(Collectors.toList());
    }
}