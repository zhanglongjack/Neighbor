package com.neighbor.app.recharge.po;

public enum RechargeStatusDesc {
    initial(1,"初始"),processing(2,"处理中"),success(3,"成功"),failed(4,"失败");

    private int value;
    private String des;

    private RechargeStatusDesc(int value, String des) {
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
        for(RechargeStatusDesc channelTypeDesc : RechargeStatusDesc.values()){
            if(channelTypeDesc.getValue()==value){
                return channelTypeDesc.getDes();
            }
        }
        return null;
    }
}
