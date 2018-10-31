package com.neighbor.app.api.common;

public enum ErrorCodeDesc {
	成功(0),失败(1);

    public int value;

    private ErrorCodeDesc(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

 
}
