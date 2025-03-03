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
        // 1. 获取当前用户并设置查询条件
        Users currentUser = userService.getCurrentUser();
        dto.setUserId(currentUser.getId());

        // 2. 分页查询订单
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        Page<Orders> page = orderMapper.queryOrders(dto);

        // 3. 批量查询所有订单项（避免 N+1 问题）
        List<Long> orderIds = page.getResult().stream()
                .map(Orders::getId)
                .collect(Collectors.toList());
        List<OrderItems> allOrderItems = orderItemMapper.selectByOrderIds(orderIds);
        Map<Long, List<OrderItems>> itemsByOrderId = allOrderItems.stream()
                .collect(Collectors.groupingBy(OrderItems::getOrderId));

        // 4. 转换为 VO 列表
        List<OrderVO> voList = page.getResult().stream()
                .map(order -> {
                    OrderVO vo = convertToOrderVO(order);
                    //vo.setItems(convertOrderItems(itemsByOrderId.get(order.getId())));
                    // 使用getOrDefault处理空items
                    vo.setItems(convertOrderItems(
                            itemsByOrderId.getOrDefault(order.getId(), Collections.emptyList())
                    ));
                    return vo;
                })
                .collect(Collectors.toList());

        return new PageResult<>(page.getTotal(), voList);

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
        // 获取当前用户的ID
        Long userId = userService.getCurrentUser().getId();

        // 根据订单ID查询订单
        Orders orders = orderMapper.queryById(orderId);
        if (orders == null) {
            throw new OrderBusinessException("订单不存在");
        }

        // 判断当前用户是否是该订单的所有者
        if (!orders.getUserId().equals(userId)) {
            throw new OrderBusinessException("无权操作该订单");
        }

        // 判断订单状态是否可以取消（假设状态 > 0 表示订单已经支付或已发货等，不允许取消）
        if (orders.getStatus() > 0) {
            throw new OrderBusinessException("当前状态不允许取消");
        }

        // 判断订单创建时间是否超过了30分钟，超过则不能取消
        if (orders.getCreatedAt() != null &&
                LocalDateTime.now().isAfter(orders.getCreatedAt().plusMinutes(30))) {
            throw new OrderBusinessException("已超过可取消时间");
        }

        // 更新订单状态为取消，设置取消时间
        orders.setStatus(-1); // -1 表示取消状态
        orders.setCancelTime(LocalDateTime.now());

        // 设置取消原因，若取消原因为空，则默认设置为“用户主动取消”
        String cancelReason = "";
        orders.setCancelReason(StringUtils.hasText(cancelReason) ? cancelReason : "用户主动取消");

        // 更新订单信息
        orderMapper.update(orders);

        // 执行取消后的处理逻辑（解锁库存,退钱）
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


//TODO 写全
//    @Transactional
//    @Override
//    public void cancelOrder(Long orderId, String cancelReason) {
//        // 1. 获取当前用户
//        Long currentUserId = userService.getCurrentUser().getId();
//
//        // 2. 查询订单（带悲观锁）
//        Orders order = orderMapper.selectForUpdate(orderId);
//        if (order == null) {
//            throw new BusinessException(404, "订单不存在");
//        }
//
//        // 3. 权限校验
//        if (!order.getUserId().equals(currentUserId)) {
//            throw new BusinessException(403, "无权操作该订单");
//        }
//
//        // 4. 状态校验
//        if (!canCancel(order.getStatus())) {
//            throw new BusinessException(409, "当前状态不允许取消");
//        }
//
//        // 5. 时效性校验（例如：支付后30分钟内可取消）
//        if (order.getPaymentTime() != null &&
//                LocalDateTime.now().isAfter(order.getPaymentTime().plusMinutes(30))) {
//            throw new BusinessException(409, "已超过可取消时间");
//        }
//
//        // 6. 更新订单
//        order.setStatus(-1);
//        order.setCancelTime(LocalDateTime.now());
//        order.setCancelReason(StringUtils.hasText(cancelReason) ?
//                cancelReason : "用户主动取消");
//        orderMapper.update(order);
//
//        // 7. 后续处理
//        afterCancelHandler(order);
//    }
//
//    // 可取消的状态：待支付(0)、已支付(1)
//    private boolean canCancel(Integer status) {
//        return status == 0 || status == 1;
//    }
//
//    // 后续处理（异步）
//    @Async
//    protected void afterCancelHandler(Orders order) {
//        // 1. 库存回滚
//        inventoryService.rollbackStock(order.getId());
//
//        // 2. 支付退款（若已支付）
//        if (order.getStatus() == 1) {
//            paymentService.refund(order.getOrderNo(), order.getActualAmount());
//        }
//
//        // 3. 记录操作日志
//        auditLogService.log(order.getId(), "订单取消");
//    }

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