package cn.mbtt.service.build;

import cn.mbtt.service.domain.dto.OrdersSubmitDTO;
import cn.mbtt.service.domain.po.OrderItems;
import cn.mbtt.service.domain.po.Orders;
import cn.mbtt.service.domain.vo.OrderVO;
import cn.mbtt.service.mapper.OrderItemMapper;
import cn.mbtt.service.mapper.OrderMapper;
import cn.mbtt.service.mapper.UserMapper;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用构建器模式（Builder Pattern）帮助构建订单及其商品项。
 *
 * 通过此构建器，能够灵活地设置订单的基本信息、订单商品项等，避免在多个类之间传递过多的参数。
 */
public class OrderItemBuilder {

    private OrdersSubmitDTO submitDTO;  // 用户提交的订单数据
    private OrderMapper orderMapper;  // 用于操作订单表的 Mapper
    private OrderItemMapper orderItemMapper;  // 用于操作订单商品项表的 Mapper
    private UserMapper userMapper;  // 用于查询用户信息的 Mapper
    private List<OrderItems> orderItemsList;  // 用于保存订单商品项
    private Orders order;  // 用于保存订单对象

    // 构造函数，初始化订单数据
    public OrderItemBuilder(OrdersSubmitDTO submitDTO, OrderMapper orderMapper, OrderItemMapper orderItemMapper, UserMapper userMapper) {
        this.submitDTO = submitDTO;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.userMapper = userMapper;
        this.orderItemsList = new ArrayList<>();
        this.order = new Orders();
    }

    /**
     * 1. 构建订单基本信息
     * 该方法会初始化订单的一些基本信息，包括订单编号、状态、创建时间等。
     */
    public OrderItemBuilder builderOrderInfo() {
        BeanUtils.copyProperties(submitDTO, order);
        // 生成订单号，模拟生成方式，可以使用更复杂的生成算法
        order.setOrderNo("ORD" + System.currentTimeMillis());
        order.setStatus(1);  // 订单初始状态，未付款
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(order.getCreatedAt());  // 初始化更新字段
        order.setTotalAmount(submitDTO.getTotalAmount());
        order.setActualAmount(submitDTO.getTotalAmount());  // 假设没有优惠，实际支付金额和总金额相同
        return this;
    }

    /**
     * 2. 构建订单商品项
     * 从购物车中获取商品数据，并根据商品数据构建订单商品项。
     */
    public OrderItemBuilder builderOrderItems() {
        // 从购物车数据中获取商品项
        List<OrderItems> cartItems = orderItemMapper.selectByOrderId(submitDTO.getUserId());

        for (OrderItems cartItem : cartItems) {
            OrderItems orderItem = new OrderItems();
            BeanUtils.copyProperties(cartItem, orderItem);
            orderItem.setOrderId(order.getId());  // 设置订单ID
            orderItemsList.add(orderItem);
        }
        return this;
    }

    /**
     * 3. 完成订单构建
     * 将订单信息保存到数据库中，并保存订单商品项。
     */
    public Orders build() {
        orderMapper.insertOrders(order);  // 保存订单到订单表
        // 插入订单商品项
        orderItemMapper.insertOrderItems(orderItemsList);
        return order;
    }

    /**
     * 4. 转换为 OrderVO（供前端展示）
     * 将订单信息和订单商品项转换为一个 OrderVO，方便返回给前端。
     */
    public OrderVO toOrderVO() {
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setTotalFee(order.getTotalAmount());  // 设置总金额（以分为单位）

        // 转换订单商品项
        List<OrderVO.OrderItemVO> itemVOList = new ArrayList<>();
        for (OrderItems item : orderItemsList) {
            OrderVO.OrderItemVO itemVO = new OrderVO.OrderItemVO();
            BeanUtils.copyProperties(item, itemVO);
            // 计算商品的总价（单价 * 数量）
            itemVO.setTotalPrice(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            itemVOList.add(itemVO);
        }
        orderVO.setItems(itemVOList);
        return orderVO;
    }
}
