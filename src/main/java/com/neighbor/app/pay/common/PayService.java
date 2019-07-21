package com.neighbor.app.pay.common;

import com.neighbor.app.pay.entity.PayNotifyResp;
import com.neighbor.app.pay.entity.QueryOrderResp;
import com.neighbor.app.recharge.entity.Recharge;

import java.util.HashMap;

public interface PayService {

    //返回页面地址
    String viewName();

     //下单
     void preOrder(Recharge recharge) throws Exception ;

     //订单查询
     QueryOrderResp queryOrder(Recharge recharge) throws Exception;

     //支付异步回调
     PayNotifyResp payNotify(HashMap<String,String> headerMap,HashMap<String,String> paramMap,String reqBody)throws Exception;
}
