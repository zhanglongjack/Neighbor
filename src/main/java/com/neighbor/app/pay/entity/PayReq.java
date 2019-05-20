package com.neighbor.app.pay.entity;

/*
appid	必填	商户通信appid
method	必填	请查看接口列表
data	必填	数组 Array
store_id	必填	（填写在data数组中）请传入空字符串
total	必填	（填写在data数组中）金额 ，单位：分，不允许包含任何符号,不支持小数点
nonce_str	必填	（填写在data数组中）随机字符串，字符范围：a-zA-Z0-9
body	必填	（填写在data数组中）商品描述，如不传请传入空字符串
out_trade_no	必填	（填写在data数组中）订单号（要保证唯一）。最大长度32位
sign	必填	请查看数据签名
 */

public class PayReq {
    private String appid;
    private String method;
    private Object data;
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }


}
