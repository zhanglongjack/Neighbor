package com.neighbor.app.balance.po;

public enum TransactionTypeDesc {
    payment("1","付款"),receipt("2","收款"),expenditure("3","支出");
    private String value;
    private String des;

    private TransactionTypeDesc(String value, String des) {
        this.value = value;
        this.des = des;
    }

    public String getValue() {
        return value;
    }

    public String getDes() {
        return des;
    }

    public static String getDesByValue(String value){
        for(TransactionTypeDesc transferStatusDesc : TransactionTypeDesc.values()){
            if(transferStatusDesc.getValue().equals(value)){
                return transferStatusDesc.getDes();
            }
        }
        return null;
    }
}
