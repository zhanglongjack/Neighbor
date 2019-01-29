package com.neighbor.app.recharge.constants;

public enum ChannelTypeDesc {
    alipay("支付宝"),wxpay("微信支付"),offline("线下转账");

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
