package com.neighbor.app.pay.common;

import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class PayConfig {

    private String apiUrl;
    private String appid;//merchantNumber = 20190701365546
    private String appkey;//81ff0c7ca96f472729dd1e18abe7bdec
    private String orgNumber;
    private String channelType = "Alipay";//通道TYPE
    private String notifyUrl;
    private String callBackUrl;
    private String wlistIp;
    private String aes;
    private String alipayPubKey;
    private String pubKey;



    private void init(String channelType){
            ResourceBundle bundlePay = ResourceBundle.getBundle("pay", new Locale(channelType, ""));
            apiUrl = bundlePay.getString("recharge.api.url");
            appid = bundlePay.getString("recharge.app.id");
            appkey = bundlePay.getString("recharge.app.key");
            orgNumber = bundlePay.getString("recharge.app.orgNumber");
            notifyUrl = bundlePay.getString("recharge.app.notifyUrl");
            callBackUrl = bundlePay.getString("recharge.app.callBackUrl");
            wlistIp = bundlePay.getString("recharge.app.wlistIp");
            aes = bundlePay.getString("recharge.app.aes");
            alipayPubKey = bundlePay.getString("recharge.app.alipay.pubKey");
            pubKey = bundlePay.getString("recharge.app.pubKey");
    }

    public void init(boolean isDev){
        if(isDev){
            setDev();
        }else{
            init("");
        }
    }

    public void init(boolean isDev,String channelType){
        if(isDev){
            setDev();
        }else{
            init(channelType);
        }
    }

    private void setDev() {
        apiUrl = "http://www.shunlianzhifu.top/Pay_Index.html";
        appid = "190808552";
        appkey = "k1c2m8pfze3lq0h2r3a81y3w83j73bdu";
        orgNumber = "330100228746";
        notifyUrl = "http://localhost:15555/pay/notify";
        callBackUrl = "http://localhost:15555/pay/callback";
        wlistIp = "127.0.0.1";
        aes = "xxxx";
        alipayPubKey ="xx";
        pubKey = "";
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

    public String getAes() {
        return aes;
    }

    public String getAlipayPubKey() {
        return alipayPubKey;
    }

    public String getPubKey() {
        return pubKey;
    }
}
