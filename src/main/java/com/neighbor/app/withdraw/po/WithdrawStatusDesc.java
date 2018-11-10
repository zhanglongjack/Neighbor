package com.neighbor.app.withdraw.po;

public enum WithdrawStatusDesc {
    initial(1,"初始"),processing(2,"出款中"),success(3,"成功"),failed(4,"失败");

    private int value;
    private String des;

    private WithdrawStatusDesc(int value, String des) {
        this.value = value;
        this.des = des;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return des;
    }
}
