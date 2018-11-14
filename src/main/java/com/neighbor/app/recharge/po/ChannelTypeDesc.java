package com.neighbor.app.recharge.po;

import com.neighbor.app.balance.po.TransactionItemDesc;

public enum ChannelTypeDesc {
    alipay("支付宝"),wxpay("微信支付");

    private String des;

    private ChannelTypeDesc(String des) {
        this.des = des;
    }


    public String getDes() {
        return des;
    }

    public static String getDesByValue(String value){
        return ChannelTypeDesc.valueOf(value).getDes();
    }

}
