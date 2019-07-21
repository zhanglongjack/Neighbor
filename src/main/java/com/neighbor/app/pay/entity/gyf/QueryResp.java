package com.neighbor.app.pay.entity.gyf;

import java.math.BigDecimal;

public class QueryResp {
    private String uNumber;
    private String merOrderId;
    private String partnerorderid;
    private BigDecimal merorderAmt;
    private BigDecimal payAmt;
    private int orderStatus;//订单状态：1 订单冻结 0 处理中， 1 订单异常， 2 支付成功
    private String orderId;
    private String message;

    public String getuNumber() {
        return uNumber;
    }

    public void setuNumber(String uNumber) {
        this.uNumber = uNumber;
    }

    public String getMerOrderId() {
        return merOrderId;
    }

    public void setMerOrderId(String merOrderId) {
        this.merOrderId = merOrderId;
    }

    public String getPartnerorderid() {
        return partnerorderid;
    }

    public void setPartnerorderid(String partnerorderid) {
        this.partnerorderid = partnerorderid;
    }

    public BigDecimal getMerorderAmt() {
        return merorderAmt;
    }

    public void setMerorderAmt(BigDecimal merorderAmt) {
        this.merorderAmt = merorderAmt;
    }

    public BigDecimal getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(BigDecimal payAmt) {
        this.payAmt = payAmt;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
