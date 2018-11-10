package com.neighbor.app.transfer.po;

import com.neighbor.app.recharge.po.ChannelTypeDesc;

public enum TransferWayDesc {
    in(1,"转入"),out(2,"转出");

    private int value;
    private String des;

    private TransferWayDesc(int value, String des) {
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
        for(TransferWayDesc transferWayDesc : TransferWayDesc.values()){
            if(transferWayDesc.getValue()==value){
                return transferWayDesc.getDes();
            }
        }
        return null;
    }
}
