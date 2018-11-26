package com.neighbor.app.withdraw.constants;

public enum WithdrawStatusDesc {
    initial("初始"),processing("出款中"),success("成功"),failed("失败");

    private String des;

    private WithdrawStatusDesc(String des) {
        this.des = des;
    }
    public String getDes() {
        return des;
    }
    public static String getDesByValue(String value){
        return WithdrawStatusDesc.valueOf(value).getDes();
    }
}
