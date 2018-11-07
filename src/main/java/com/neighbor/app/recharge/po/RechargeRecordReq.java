package com.neighbor.app.recharge.po;

import com.neighbor.app.api.po.PageReq;

public class RechargeRecordReq extends PageReq {
    private Long uId;
    private String orderNo;

    public Long getuId() {
        return uId;
    }

    public void setuId(Long uId) {
        this.uId = uId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
