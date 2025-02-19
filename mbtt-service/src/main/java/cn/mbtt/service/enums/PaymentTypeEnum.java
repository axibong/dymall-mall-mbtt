package cn.mbtt.service.enums;

import cn.mbtt.common.exception.BizIllegalException;

public enum PaymentTypeEnum {
    ALIPAY(1, "支付宝"),
    WECHAT(2,"微信"),
    CREDIT_CARD(3,"信用卡")
    ;
    private Integer code;

    private String msg;

    PaymentTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static String getPaymentTypeByCode(Integer code)
    {
        for (PaymentTypeEnum value : PaymentTypeEnum.values()) {
            if (value.getCode() == code)
            {
                return value.name();
            }
        }
        throw new BizIllegalException("支付方式不存在");
    }
}
