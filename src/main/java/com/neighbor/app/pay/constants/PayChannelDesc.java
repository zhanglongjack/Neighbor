package com.neighbor.app.pay.constants;

import com.neighbor.app.pay.service.GyfPayService;

public enum PayChannelDesc {
    //国易付
    pay_gyf(GyfPayService.class);

    private Class des;

    private PayChannelDesc(Class des) {
        this.des = des;
    }


    public Class getDes() {
        return des;
    }

    public static Class getDesByValue(String value){
        return PayChannelDesc.valueOf(value).getDes();
    }
}
