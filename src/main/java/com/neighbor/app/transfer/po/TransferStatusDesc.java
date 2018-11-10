package com.neighbor.app.transfer.po;

public enum TransferStatusDesc {
    initial(1,"初始"),processing(2,"处理中"),success(3,"成功"),failed(4,"失败");

    private int value;
    private String des;

    private TransferStatusDesc(int value, String des) {
        this.value = value;
        this.des = des;
    }

    public int getValue() {
        return value;
    }

    public String getDes() {
        return des;
    }

    public static String getDesByValue(int value){
        for(TransferStatusDesc transferStatusDesc : TransferStatusDesc.values()){
            if(transferStatusDesc.getValue()==value){
                return transferStatusDesc.getDes();
            }
        }
        return null;
    }
}
