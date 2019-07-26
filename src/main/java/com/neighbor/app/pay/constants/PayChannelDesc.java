package com.neighbor.app.pay.constants;

import com.neighbor.app.pay.service.AlipayService;
import com.neighbor.app.pay.service.GyfPayService;

public enum PayChannelDesc {
    //国易付,支付宝APP支付
    pay_gyf(GyfPayService.class),pay_alipay_app(AlipayService.class);

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
