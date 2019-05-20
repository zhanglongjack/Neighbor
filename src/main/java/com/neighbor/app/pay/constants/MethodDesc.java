package com.neighbor.app.pay.constants;

public enum MethodDesc {
    wx_native("微信二维码支付"),wx_jsapi("微信公众号支付"),ali_native("支付宝二维码支付")
    ,ali_h5("支付宝H5"),union_scan("银联扫码"),heepay_union("银联WAP"),order_query("订单查询")
    ,order_data("商户流水查询"),offline("线下转账");

    private String des;

    private MethodDesc(String des) {
        this.des = des;
    }


    public String getDes() {
        return des;
    }

    public static String getDesByValue(String value){
        return MethodDesc.valueOf(value).getDes();
    }

}
