package com.neighbor.app.balance.po;

public enum TransactionTypeDesc {
    payment("付款"),receipt("收款"),expenditure("支出"),income("收入");
    private String des;

    private TransactionTypeDesc(String des) {
        this.des = des;
    }


    public String getDes() {
        return des;
    }

    public static String getDesByValue(String value){
        return TransactionTypeDesc.valueOf(value).getDes();
    }
}
