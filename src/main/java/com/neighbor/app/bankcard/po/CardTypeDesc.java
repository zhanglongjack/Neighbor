package com.neighbor.app.bankcard.po;

public enum CardTypeDesc {
    DC("储蓄卡"),CC("信用卡"),SCC("准贷记卡"),PC("预付费卡");

    private String des;

    private CardTypeDesc(String des) {
        this.des = des;
    }
    public String getDes() {
        return des;
    }
    public static String getDesByValue(String value){
        return CardTypeDesc.valueOf(value).getDes();
    }
}
