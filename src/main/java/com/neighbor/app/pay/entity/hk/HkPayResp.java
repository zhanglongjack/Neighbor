package com.neighbor.app.pay.entity.hk;

public class HkPayResp {
    private String amount;
    private String out_trade_no;
    private String order_sn;
    private String realAmount;
    private Integer orderStatus;
    private String callback_time;


    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(String realAmount) {
        this.realAmount = realAmount;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCallback_time() {
        return callback_time;
    }

    public void setCallback_time(String callback_time) {
        this.callback_time = callback_time;
    }
}
