package com.neighbor.app.pay.entity;

public class PayNotifyResp {

    private Long uId;//用户Id
    private String ack = "OK";//应答字符

    public Long getuId() {
        return uId;
    }

    public void setuId(Long uId) {
        this.uId = uId;
    }

    public String getAck() {
        return ack;
    }

    public void setAck(String ack) {
        this.ack = ack;
    }
}
