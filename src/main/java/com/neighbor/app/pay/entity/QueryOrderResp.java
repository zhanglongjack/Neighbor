package com.neighbor.app.pay.entity;

public class QueryOrderResp {
    private int orderStatus;//订单状态：1 订单冻结 0 处理中， 1 订单异常， 2 支付成功
    private String message;//消息

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
