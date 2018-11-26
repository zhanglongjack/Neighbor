package com.neighbor.app.transfer.constants;


public enum TransferWayDesc {
    in("转入"),out("转出");
    private String des;

    private TransferWayDesc( String des) {
        this.des = des;
    }
    public String getDes() {
        return des;
    }

    public static String getDesByValue(String value){
        return TransferWayDesc.valueOf(value).getDes();
    }
}
