package cn.mbtt.service.service;
import cn.mbtt.service.domain.dto.PayReqDTO;
import cn.mbtt.service.domain.vo.BasePayResultVO;

public interface PayService {
    BasePayResultVO pay(PayReqDTO payReqDTO);
}
