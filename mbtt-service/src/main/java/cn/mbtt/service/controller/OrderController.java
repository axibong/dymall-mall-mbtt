package cn.mbtt.service.controller;

import cn.mbtt.common.result.CommonPage;
import cn.mbtt.common.result.CommonResult;
import cn.mbtt.common.result.PageResult;
import cn.mbtt.service.domain.dto.OrdersPageQueryDTO;
import cn.mbtt.service.domain.dto.OrdersSubmitDTO;
import cn.mbtt.service.domain.vo.OrderVO;
import cn.mbtt.service.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "订单相关接口")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 根据用户分页查询订单
     */
    @ApiOperation("订单分页查询")
    @PostMapping("/page")
    public CommonResult<CommonPage<OrderVO>> queryOrders(@RequestBody OrdersPageQueryDTO dto) {
        PageResult<OrderVO> pageResult = orderService.queryOrders(dto);
        return CommonResult.success(CommonPage.restPage(
                pageResult.getData()
        ));
    }

    @ApiOperation("创建订单")
    @PostMapping("/create")
    public CommonResult<OrderVO> createOrder(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        OrderVO orderVO = orderService.createOrder(ordersSubmitDTO);
        return CommonResult.success(orderVO);
    }

    //TODO 之后和支付接口对接，看需要传入哪些数据
//    @ApiOperation("更新订单支付状态")
//    @PutMapping("/{orderId}/payment")
//    public CommonResult<OrderVO> updatePaymentStatus(@PathVariable Long orderId, @RequestBody PaymentStatusDTO paymentStatusDTO) {
//        OrderVO updatedOrder = orderService.updatePaymentStatus(orderId, paymentStatusDTO);
//        return CommonResult.success(updatedOrder);
//    }

    //TODO
//    @ApiOperation("取消订单")
//    @PutMapping("/{orderId}/cancel")
//    public CommonResult<Void> cancelOrder(@PathVariable Long orderId) {
//        orderService.cancelOrder(orderId);
//        return CommonResult.success(null);
//    }


}