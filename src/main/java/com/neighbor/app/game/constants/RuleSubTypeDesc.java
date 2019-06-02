package com.neighbor.app.game.constants;

public enum RuleSubTypeDesc {
    //子类型
    single(1, "单个值"), continuity(2,"顺子"), same(3,"同数"), leopard(4,"豹子");
    private int value;
    private String des;
    private RuleSubTypeDesc(int value, String des) {
        this.value = value;
        this.des = des;
    }

    public String getDes() {
        return des;
    }

    public static String getRuleSubTypeStr(Integer i){
        if(i==1){
            return RuleSubTypeDesc.single.getDes();
        }else if(i==2){
            return RuleSubTypeDesc.continuity.getDes();
        }else if(i==3){
            return RuleSubTypeDesc.same.getDes();
        }else if(i==4){
            return RuleSubTypeDesc.leopard.getDes();
        }
        return null;
    }

    public int getValue() {
        return value;
    }

}
