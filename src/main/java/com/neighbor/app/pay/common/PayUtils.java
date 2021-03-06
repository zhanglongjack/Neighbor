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
import com.neighbor.common.util.DateFormateType;
import com.neighbor.common.util.DateUtils;
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
    private String callBackUrl;


    private void init(){
    	if(apiUrl==null){
            apiUrl = env.getProperty("recharge.api.url");
            appid = env.getProperty("recharge.app.id");
            appkey = env.getProperty("recharge.app.key");
            orgNumber = env.getProperty("recharge.app.orgNumber");
            notifyUrl = env.getProperty("recharge.app.notifyUrl");
            callBackUrl = env.getProperty("recharge.app.callBackUrl");
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

    public void preOrderXF(Recharge recharge) throws Exception{
        init();
        //构造data参数 加密
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("pay_memberid",appid);
        hashMap.put("pay_orderid",recharge.getOrderNo());
        hashMap.put("pay_applydate", DateUtils.formatDateStr(new Date(), DateFormateType.LANG_FORMAT));
        hashMap.put("pay_bankcode",recharge.getChannelNo());
        hashMap.put("pay_notifyurl",notifyUrl);
        hashMap.put("pay_callbackurl",callBackUrl);
        hashMap.put("pay_amount",recharge.getAmount().toPlainString());
        String signStr =  EncodeData.encode(putPairsSequenceAndTogether(hashMap)+"&key="+appkey).toUpperCase();
        hashMap.put("pay_md5sign",signStr);
        hashMap.put("pay_productname",recharge.getBody());
        hashMap.put("payUrl",apiUrl);
        String reqStr = JSON.toJSONString(hashMap);
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

    public boolean validParam(HashMap<String, String> notifyResp){
        String sign = notifyResp.remove("sign");
        String attach = notifyResp.remove("attach");
        String signTemp = putPairsSequenceAndTogether(notifyResp);
        System.out.println(signTemp);
        String signStr = EncodeData.encode(putPairsSequenceAndTogether(notifyResp)+"&key="+appkey).toUpperCase();
        if(sign==null){
            return false;
        }
       if(sign.equals(signStr)){
           return appid.equals(notifyResp.get("memberid"));
       };
       return false;
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
       HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("memberid","190824287");
        hashMap.put("orderid","20190831041524832217");
        hashMap.put("amount", "99");
        hashMap.put("transaction_id","123456789");
        hashMap.put("datetime",DateUtils.formatDateStr(new Date(), DateFormateType.LANG_FORMAT));
        hashMap.put("returncode","00");
        String key = "vfbd5ldmxxzk92g3ktjjqpop057cmah1";
        String signStr =  EncodeData.encode(putPairsSequenceAndTogether(hashMap)+"&key="+key).toUpperCase();
        hashMap.put("sign",signStr);
        hashMap.put("attach","11beb49989ad4525971ab320ab9bc4ea");
        String respStr = HttpClientUtils.httpPostWithPAaram("http://localhost:9527/pay/notify",hashMap);

        System.out.println("hk pay resp str <== "+respStr);


     /*  String jsonStr = "{\n" +
               "  \"gmt_create\": \"2019-07-25 22:35:43\",\n" +
               "  \"charset\": \"UTF-8\",\n" +
               "  \"seller_email\": \"18345041520\",\n" +
               "  \"subject\": \"积分充值：0.01元\",\n" +
               "  \"sign\": \"kgFvgAVD3S0nt3jlOKpIBpSsFrsLixR3Y6GFy1hrxtqJ5gue6BTrRw5+cgdRCZGsmUHl515j61kTqDO/glbbj0B9z0Fi/Jab+065kHW8xnYA1przKsltVgUQWE4gkYo3ZXkeLAH287+zrkj5Z4ynjFvx+xbykeCBmVtb4ZZq0w17voio9PPubZKZQJYqFWaL/HJAzrs89BviORlvEC3yMn9k7VQQoskwHDm+WlgPhyBojcqLJUQEmJy22O+N7OErV340T8ks83X8FaxsOpJNXphFXLFyT89I7BBod1EuJR6wgNsRSevLac+A0ieUZxULgybjL84bZcWsAZ8Fv/Jqhw==\",\n" +
               "  \"buyer_id\": \"2088802788364260\",\n" +
               "  \"invoice_amount\": \"0.01\",\n" +
               "  \"notify_id\": \"2019072500222223545064260569680853\",\n" +
               "  \"fund_bill_list\": \"[{\\\"amount\\\":\\\"0.01\\\",\\\"fundChannel\\\":\\\"ALIPAYACCOUNT\\\"}]\",\n" +
               "  \"notify_type\": \"trade_status_sync\",\n" +
               "  \"trade_status\": \"TRADE_SUCCESS\",\n" +
               "  \"receipt_amount\": \"0.01\",\n" +
               "  \"app_id\": \"2019072565964702\",\n" +
               "  \"buyer_pay_amount\": \"0.01\",\n" +
               "  \"sign_type\": \"RSA2\",\n" +
               "  \"seller_id\": \"2088532778903774\",\n" +
               "  \"gmt_payment\": \"2019-07-25 22:35:44\",\n" +
               "  \"notify_time\": \"2019-07-25 22:49:07\",\n" +
               "  \"version\": \"1.0\",\n" +
               "  \"out_trade_no\": \"260001720190725103531937\",\n" +
               "  \"total_amount\": \"0.01\",\n" +
               "  \"trade_no\": \"2019072522001464260523755886\",\n" +
               "  \"auth_app_id\": \"2019072565964702\",\n" +
               "  \"buyer_logon_id\": \"177****8792\",\n" +
               "  \"point_amount\": \"0.00\"\n" +
               "}";
       HashMap<String,String> map = JSON.parseObject(jsonStr,HashMap.class);
        System.out.println(putPairsSequenceAndTogether(map));*/

        /*String reqStr = "{\"wlistIp\":\"127.0.0.1\",\"orderId\":\"260001720190722044822816\",\"uNumber\":\"xT0t5q9i\",\"sign\":\"03CE0810A58CDF46621BE4688B0AD98A\",\"notifyUrl\":\"http://t.auth.gtlytech.com:15555/pay/notify\",\"merId\":\"421109B6BC00000\",\"payChannel\":\"sm_wechat\",\"remark\":\"600017\",\"payUrl\":\"http://interface.9111pay.com:8443/trade/pay.trade.pay.action\",\"orderAmt\":\"10000\",\"returnUrl\":\"http://t.auth.gtlytech.com:15555/pay/callback\"}";
        String host = "http://interface.9111pay.com:8443/trade/pay.trade.pay.action";
        String respStr = HttpClientUtils.httpPostWithPAaram(host,JSON.parseObject(reqStr,HashMap.class));

        System.out.println(respStr);*/
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

    public static void preOrderXF() throws Exception{
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("pay_memberid","190808552");
        hashMap.put("pay_orderid",OrderUtils.getOrderNo(OrderUtils.RECHARGE,6000000L));
        hashMap.put("pay_applydate", DateUtils.formatDateStr(new Date(), DateFormateType.LANG_FORMAT));
        hashMap.put("pay_bankcode","941");
        hashMap.put("pay_notifyurl","http://localhost:15555/pay/notify");
        hashMap.put("pay_callbackurl","http://localhost:15555/pay/notify");
        hashMap.put("pay_amount","50");
        String key = "k1c2m8pfze3lq0h2r3a81y3w83j73bdu";
        String signStr =  EncodeData.encode(putPairsSequenceAndTogether(hashMap)+"&key="+key).toUpperCase();
        hashMap.put("pay_md5sign",signStr);
        hashMap.put("pay_productname","goods");
        String reqStr = JSON.toJSONString(hashMap);
        System.out.println("xf pay req str ==> "+reqStr);
        String respStr = HttpClientUtils.httpPostWithPAaram("http://www.shunlianzhifu.top/Pay_Index.html",hashMap);
        System.out.println("xf pay resp str <== "+respStr);
    }
}
