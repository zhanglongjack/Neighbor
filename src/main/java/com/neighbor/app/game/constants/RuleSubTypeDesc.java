package com.neighbor.app.game.constants;

public enum RuleSubTypeDesc {
    //子类型
    single(1, "单个值"), continuity(2,"顺子"), same(3,"同数");
    private int value;
    private String des;
    private RuleSubTypeDesc(int value, String des) {
        this.value = value;
        this.des = des;
    }

    public int getValue() {
        return value;
    }

}
