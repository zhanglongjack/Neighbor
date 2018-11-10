package com.neighbor.app.recharge.po;

public enum ChannelTypeDesc {
    alipay(1,"支付宝"),wxpay(2,"微信支付");

    private int value;
    private String des;

    private ChannelTypeDesc(int value,String des) {
        this.value = value;
        this.des = des;
    }

    public int getValue() {
        return value;
    }

    public String getDes() {
        return des;
    }

    public static String getDesByValue(int value){
        for(ChannelTypeDesc channelTypeDesc : ChannelTypeDesc.values()){
            if(channelTypeDesc.getValue()==value){
                return channelTypeDesc.getDes();
            }
        }
        return null;
    }

}
