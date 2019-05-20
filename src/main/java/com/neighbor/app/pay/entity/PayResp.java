package com.neighbor.app.pay.entity;
/*
code	必填	返回状态码参考返回码
msg	必填	返回码信息提示
data	必填	数组 Array
result_code	必填	（在data数组中）下单结果状态码0000为成功，其他为失败
result_msg	必填	（在data数组中）下单结果描述
method	必填	（在data数组中）支付方式
out_trade_no	必填	（在data数组中）平台交易单号
u_out_trade_no	必填	（在data数组中）商户订单号
code_url	必填	（在data数组中）二维码链接地址
total	必填	（在data数组中）订单金额 单位：分
create_time	必填	（在data数组中）订单创建时间戳
sign	必填	签名
 */
public class PayResp {
    private Integer code;
    private String msg;
    private PayRespData data;
    private String sign;

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

    public PayRespData getData() {
        return data;
    }

    public void setData(PayRespData data) {
        this.data = data;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
