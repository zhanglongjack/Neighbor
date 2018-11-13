package com.neighbor.app.balance.po;

public enum TransactionItemDesc {
    transfer("1","转账"),recharge("2","充值"),withdraw("3","取现");
    private String value;
    private String des;

    private TransactionItemDesc(String value, String des) {
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
        for(TransactionItemDesc transferStatusDesc : TransactionItemDesc.values()){
            if(transferStatusDesc.getValue().equals(value)){
                return transferStatusDesc.getDes();
            }
        }
        return null;
    }
}
