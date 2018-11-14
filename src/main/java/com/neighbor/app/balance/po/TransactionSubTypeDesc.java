package com.neighbor.app.balance.po;

public enum TransactionSubTypeDesc {
    //付款
    sendRedPack("发红包"),transferOut("转给"),thunderOut("踩雷")
    //收款
    ,receiveRedPack("收红包"),backRedPack("红包退回"),lucked("中奖"),thunderIn("收到雷包"),recharge("充值")
    ,transferIn("转入")
    //支出
    ,withdraw("提现"),withdrawCost("提现手续费");
    private String des;

    private TransactionSubTypeDesc( String des) {
        this.des = des;
    }


    public String getDes() {
        return des;
    }

    public static String getDesByValue(String value){
        return TransactionSubTypeDesc.valueOf(value).getDes();
    }
}
