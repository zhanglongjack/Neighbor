package com.neighbor.app.pay.common;

import com.alibaba.fastjson.JSON;
import com.neighbor.app.common.util.OrderUtils;
import com.neighbor.app.pay.entity.PayResp;
import com.neighbor.app.pay.entity.PayScan;
import com.neighbor.app.pay.entity.PayScanReq;
import com.neighbor.app.pay.entity.hk.HkPayData;
import com.neighbor.app.pay.entity.hk.HkPayReq;
import com.neighbor.app.pay.entity.hk.HkPayResp;
import com.neighbor.app.recharge.entity.Recharge;
import com.neighbor.common.security.EncodeData;
import com.neighbor.common.util.HttpClientUtils;
import com.neighbor.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class PayUtils {
    private final static Logger logger = LoggerFactory.getLogger(PayUtils.class);
    @Autowired
    private Environment env;

    private String apiUrl;
    private String appid;//merchantNumber = 20190701365546
    private String appkey;//81ff0c7ca96f472729dd1e18abe7bdec
    private String orgNumber;
    private String channelType = "Alipay";//通道TYPE
    private String notifyUrl;


    private void init(){
    	if(apiUrl==null){
            apiUrl = env.getProperty("recharge.api.url");
            appid = env.getProperty("recharge.app.id");
            appkey = env.getProperty("recharge.app.key");
            orgNumber = env.getProperty("recharge.app.orgNumber");
            notifyUrl = env.getProperty("recharge.app.notifyUrl");
    	}
    }
    
    public void init(boolean isDev){
    	if(isDev){
            apiUrl = "http://www.hongkongpay.info/api/request/query/";
            appid = "20190701365546";
            appkey = "81ff0c7ca96f472729dd1e18abe7bdec";
            orgNumber = "330100228746";
            notifyUrl = "http://t.auth.gtlytech.com:15555/pay/notify";
    	}else{
    		init();
    	}
    }

    public PayResp preOrder(Recharge recharge) throws Exception{
        init();
        PayScanReq payScanReq = new PayScanReq();
        PayScan payScan = new PayScan();
        payScanReq.setAppid(appid);
        payScanReq.setMethod(recharge.getMethod());
        HashMap<String, String> data = new HashMap<>();
        data.put("store_id","");
        payScan.setStore_id("");
        long total = recharge.getAmount().multiply(new BigDecimal("100")).longValue();
        data.put("total",total+"");
        payScan.setTotal(total);
        String nonce_str = UUID.randomUUID().toString();
        data.put("nonce_str",nonce_str);
        payScan.setNonce_str(nonce_str);
        //String body = StringUtil.isNotEmpty(recharge.getBody())?recharge.getBody():"积分充值："+recharge.getAmount();
        //body = URLEncoder.encode(body,"utf-8");
        String body = "";
        payScan.setBody(body);
        data.put("body",body);
        String orderNo = recharge.getOrderNo();
        data.put("out_trade_no",orderNo);
        payScan.setOut_trade_no(orderNo);

        String signStr = putPairsSequenceAndTogether(data)+"&key="+appkey;
        logger.info("signStr  >> "+signStr);
        System.out.println(signStr);
        payScanReq.setData(payScan);
        payScanReq.setSign(EncodeData.encode(signStr).toUpperCase());
        String reqStr = JSON.toJSONString(payScanReq);
        PayResp payResp = JSON.parseObject(HttpClientUtils.doJsonPost(reqStr,apiUrl),PayResp.class);
        return payResp;
    }

    public void preOrderHk(Recharge recharge) throws Exception{
        init();
        //构造data参数 加密
        HkPayData data = new HkPayData();
        Integer total = recharge.getAmount().multiply(new BigDecimal("100")).intValue();
        data.setAmount(total);
        data.setGame_id(recharge.getuId().longValue()+"");
        String orderNo = recharge.getOrderNo();
        data.setOut_trade_no(orderNo);
        data.setGoodsName(recharge.getBody());
        data.setNotifyUrl(notifyUrl);
        String dataJsonStr = JSON.toJSONString(data);
        logger.info("data json ==> "+dataJsonStr);
        String encryptData = AesUtil.encryptData(dataJsonStr, appkey);
        logger.info("data encrypt ==> "+encryptData);
        //进行签名
        HkPayReq hkPayReq = new HkPayReq();
        hkPayReq.setOrg_number(orgNumber);
        hkPayReq.setMerchant_number(appid);
        hkPayReq.setPayType(recharge.getMethod());
        hkPayReq.setData(encryptData);
        hkPayReq.setSign(transactionSign(hkPayReq,appkey));
        hkPayReq.setPayUrl(apiUrl);
        String reqStr = JSON.toJSONString(hkPayReq);
        logger.info("hk pay req str ==> "+reqStr);
        recharge.setCodeUrl(reqStr);

    }

    /**
     * 生成签名，根据字段名ascii码，从小大到大
     * @param info
     * @return
     */
    public static String putPairsSequenceAndTogether(Map<String, String> info) {
        List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(info.entrySet());
        Collections.sort(infoIds, new MapComparator());
        String ret = "";
        for (Map.Entry<String, String> entry : infoIds) {
            if(StringUtil.isEmpty(entry.getValue()))continue;
            ret += entry.getKey();
            ret += "=";
            ret += entry.getValue();
            ret += "&";
        }
        ret = ret.substring(0, ret.length() - 1);
        return ret;
    }


    public static String transactionSign(HkPayReq hkPayReq,String appkey){
        String signStr = hkPayReq.getOrg_number()+hkPayReq.getMerchant_number()+hkPayReq.getPayType()+hkPayReq.getData()+appkey;
        System.out.println("singStr ==> "+signStr);
       return EncodeData.encode(signStr);
    }

    public boolean validSign(HkPayReq hkPayReq,String appkey){
        String signStr = EncodeData.encode(hkPayReq.getOrg_number()+hkPayReq.getMerchant_number()+hkPayReq.getPayType()+hkPayReq.getData()+appkey);
        return signStr.equals(hkPayReq.getSign());
    }

    public boolean validParam(HkPayReq hkPayReq){
        if(!orgNumber.equals(hkPayReq.getOrg_number())) return false;
        if(!appid.equals(hkPayReq.getMerchant_number())) return false;
        return validSign(hkPayReq,appkey);
    }

    public HkPayResp decryptResp(HkPayReq hkPayReq){
        return JSON.parseObject(AesUtil.decryptData(hkPayReq.getData(),appkey),HkPayResp.class);
    }

    
    public static void main(String[] args) throws Exception {
        String reqStr = "{\"payType\":\"Alipay\",\"data\":\"CDE2064F0AAC0BFB2214D1B241046FB3497D8B737A42238A18A90F14D734E6957A68807ED472663B5D8C9C2736F1362B73A281B7B758DA17218544F728FBE94CAC87771D7C6FC4394FEBDCEA64DE3BFD98289100C0E2EE1BC07DC6FFAF147A5838EC351310B848EC71C53B2B6E2969EFEE03573C92610499D6727419B9BCBFFB06C0B7CC68681B474FB6234CF5A6E4E7\",\"org_number\":\"330100228746\",\"sign\":\"596c4307571eb260349e7789666b4fc0\",\"merchant_number\":\"20190701365546\"}";
        System.out.println("hk pay req str ==> "+reqStr);
        HashMap<String,String> param = JSON.parseObject(reqStr,HashMap.class);
        String respStr = HttpClientUtils.httpPostWithPAaram("http://localhost:15555/pay/notify",param);
        //String respStr = HttpClientUtils.doJsonPost(reqStr,pay.apiUrl);
        System.out.println("hk pay resp str <== "+respStr);
	}

	public static void testOrder() throws Exception{
        PayUtils pay = new PayUtils();
        pay.init(true);
        //构造data参数 加密
        HkPayData data = new HkPayData();
        data.setAmount(10003);
        data.setGame_id("6000000");
        data.setOut_trade_no(OrderUtils.getOrderNo(OrderUtils.RECHARGE,6000000L));
        data.setGoodsName("积分充值100");
        data.setNotifyUrl(pay.notifyUrl);
        String dataJsonStr = JSON.toJSONString(data);
        System.out.println("data json ==> "+dataJsonStr);
        String encryptData = AesUtil.encryptData(dataJsonStr,  pay.appkey);
        System.out.println("data encrypt ==> "+encryptData);
        //进行签名
        HkPayReq hkPayReq = new HkPayReq();
        hkPayReq.setOrg_number(pay.orgNumber);
        hkPayReq.setMerchant_number(pay.appid);
        hkPayReq.setPayType(pay.channelType);
        hkPayReq.setData(encryptData);
        hkPayReq.setSign(transactionSign(hkPayReq,pay.appkey));
        String reqStr = JSON.toJSONString(hkPayReq);
        System.out.println("hk pay req str ==> "+reqStr);
        HashMap<String,String> param = JSON.parseObject(reqStr,HashMap.class);
        String respStr = HttpClientUtils.httpPostWithPAaram(pay.apiUrl,param);
        //String respStr = HttpClientUtils.doJsonPost(reqStr,pay.apiUrl);
        System.out.println("hk pay resp str <== "+respStr);
    }

}
