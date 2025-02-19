package cn.mbtt.service.enums;

public enum PayStatusEnum {
    INIT(0, "待支付"),
    PAYSUCCESS(1, "支付成功"),
    PAYFAIL(2,"支付失败"),
    REFUND(3,"已退款")
    ;
    PayStatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
