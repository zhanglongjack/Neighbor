package com.neighbor.app.balance.po;

public enum TransactionItemDesc {
    transfer("转账"),recharge("充值"),withdraw("取现");
    private String des;

    private TransactionItemDesc(String des) {
        this.des = des;
    }

    public String getDes() {
        return des;
    }

    public static String getDesByValue(String value){
        return TransactionItemDesc.valueOf(value).getDes();
    }
}
