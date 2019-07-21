package com.neighbor.app.pay.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PayConfig {
    private final static Logger logger = LoggerFactory.getLogger(PayConfig.class);
    @Autowired
    private Environment env;

    private String apiUrl;
    private String appid;//merchantNumber = 20190701365546
    private String appkey;//81ff0c7ca96f472729dd1e18abe7bdec
    private String orgNumber;
    private String channelType = "Alipay";//通道TYPE
    private String notifyUrl;
    private String callBackUrl;
    private String wlistIp;


    private void init(){
        if(apiUrl==null){
            apiUrl = env.getProperty("recharge.api.url");
            appid = env.getProperty("recharge.app.id");
            appkey = env.getProperty("recharge.app.key");
            orgNumber = env.getProperty("recharge.app.orgNumber");
            notifyUrl = env.getProperty("recharge.app.notifyUrl");
            callBackUrl = env.getProperty("recharge.app.callBackUrl");
            wlistIp = env.getProperty("recharge.app.wlistIp");
        }
    }

    public void init(boolean isDev){
        if(isDev){
            apiUrl = "http://www.shunlianzhifu.top/Pay_Index.html";
            appid = "190808552";
            appkey = "k1c2m8pfze3lq0h2r3a81y3w83j73bdu";
            orgNumber = "330100228746";
            notifyUrl = "http://localhost:15555/pay/notify";
            callBackUrl = "http://localhost:15555/pay/callback";
            wlistIp = "127.0.0.1";
        }else{
            init();
        }
    }


    public String getApiUrl() {
        return apiUrl;
    }

    public String getAppid() {
        return appid;
    }

    public String getAppkey() {
        return appkey;
    }

    public String getOrgNumber() {
        return orgNumber;
    }

    public String getChannelType() {
        return channelType;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public String getWlistIp() {
        return wlistIp;
    }
}
