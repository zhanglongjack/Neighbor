package com.neighbor.app.game.constants;

public enum RuleTypeDesc {
    //返佣规则,中奖规则,中雷规则
    rebate(1, "返佣规则"), award(2,"中奖规则"), thunder(3,"中雷规则");
    private int value;
    private String des;
    private RuleTypeDesc(int value,String des) {
        this.value = value;
        this.des = des;
    }

    public int getValue() {
        return value;
    }

}
