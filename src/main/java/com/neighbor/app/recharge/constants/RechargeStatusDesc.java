package com.neighbor.app.recharge.constants;

public enum RechargeStatusDesc {
    initial("初始"),processing("处理中"),success("成功"),failed("失败");

    private String des;

    private RechargeStatusDesc( String des) {
        this.des = des;
    }


    public String getDes() {
        return des;
    }

    public static String getDesByValue(String value){
        return RechargeStatusDesc.valueOf(value).getDes();
    }
  
}
