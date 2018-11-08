package com.neighbor.app.api.common;

public enum ErrorCodeDesc {
	success(0,"成功"),failed(1,"失败");

    private int value;
    private String des;

    private ErrorCodeDesc(int value,String des) {
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
