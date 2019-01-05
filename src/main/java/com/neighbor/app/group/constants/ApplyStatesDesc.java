package com.neighbor.app.group.constants;

public enum ApplyStatesDesc {
    //状态，1：申请；2：审核通过；3：审核拒绝；
    apply(1, "申请"), pass(2,"审核通过"), reject(3,"审核拒绝");
    private int value;
    private String des;
    private ApplyStatesDesc(int value, String des) {
        this.value = value;
        this.des = des;
    }

    public int getValue() {
        return value;
    }

}
