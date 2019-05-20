package com.neighbor.app.pay.entity;

public class PayScanReq {
    private String appid;
    private String method;
    private PayScan data;
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

    public PayScan getData() {
        return data;
    }

    public void setData(PayScan data) {
        this.data = data;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
