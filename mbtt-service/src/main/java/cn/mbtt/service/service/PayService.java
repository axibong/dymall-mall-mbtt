package cn.mbtt.service.service;
import cn.mbtt.service.domain.dto.PayReqDTO;
import cn.mbtt.service.domain.po.Orders;
import cn.mbtt.service.domain.vo.BasePayResultVO;

import javax.servlet.http.HttpServletRequest;

public interface PayService {
    BasePayResultVO pay(PayReqDTO payReqDTO);

    String payNotify(HttpServletRequest request);

    Orders getNewOrder(String orderNo);
}
