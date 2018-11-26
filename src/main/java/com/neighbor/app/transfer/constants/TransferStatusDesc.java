package com.neighbor.app.transfer.constants;

public enum TransferStatusDesc {
    initial("初始"),processing("处理中"),success("成功"),failed("失败");

    private String des;

    private TransferStatusDesc( String des) {
        this.des = des;
    }



    public String getDes() {
        return des;
    }

    public static String getDesByValue(String value){
        return TransferStatusDesc.valueOf(value).getDes();
    }
}
