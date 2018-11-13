package com.neighbor.app.balance.po;

public enum TransactionSubTypeDesc {
    //付款
    sendRedPack("1","发红包"),transferOut("2","转给"),thunderOut("3","踩雷")
    //收款
    ,receiveRedPack("4","收红包"),backRedPack("4","红包退回"),lucked("5","中奖"),thunderIn("6","收到雷包"),recharge("7","充值")
    ,transferIn("8","转入")
    //支出
    ,withdraw("9","提现"),withdrawCost("10","提现手续费");
    private String value;
    private String des;

    private TransactionSubTypeDesc(String value, String des) {
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
        for(TransactionSubTypeDesc transferStatusDesc : TransactionSubTypeDesc.values()){
            if(transferStatusDesc.getValue().equals(value)){
                return transferStatusDesc.getDes();
            }
        }
        return null;
    }
}
