package com.neighbor.app.recharge.constants;

public enum RechargeStatusDesc {
    initial("待审核"),processing("处理中"),success("成功"),failed("失败"),error("异常");

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

    public static String getDesByInt(int state){
        if(state==0){
            return RechargeStatusDesc.failed.toString();
        }else if(state==1){
            return RechargeStatusDesc.success.toString();
        }else if(state==2){
            return RechargeStatusDesc.processing.toString();
        }else if(state==3){
            return RechargeStatusDesc.error.toString();
        }
        return RechargeStatusDesc.initial.toString();
    }
  
}
