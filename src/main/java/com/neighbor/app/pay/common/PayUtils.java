package com.neighbor.app.pay.common;

import com.alibaba.fastjson.JSON;
import com.neighbor.app.pay.entity.PayResp;
import com.neighbor.app.pay.entity.PayScan;
import com.neighbor.app.pay.entity.PayScanReq;
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
import java.net.URLEncoder;
import java.util.*;

@Component
public class PayUtils {
    private final static Logger logger = LoggerFactory.getLogger(PayUtils.class);
    @Autowired
    private Environment env;

    private String apiUrl;
    private String appid;
    private String appkey;

    private void init(){
        apiUrl = env.getProperty("recharge.api.url");
        appid = env.getProperty("recharge.app.id");
        appkey = env.getProperty("recharge.app.key");
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
        String body = StringUtil.isNotEmpty(recharge.getBody())?recharge.getBody():"积分充值："+recharge.getAmount();
        body = URLEncoder.encode(body,"utf-8");
        payScan.setBody(body);
        data.put("body",body);
        String orderNo = recharge.getOrderNo();
        data.put("out_trade_no",orderNo);
        payScan.setOut_trade_no(orderNo);
        String signStr = putPairsSequenceAndTogether(data)+"&key="+appkey;
        logger.info("signStr  >> "+signStr);
        payScanReq.setData(payScan);
        payScanReq.setSign(EncodeData.encode(signStr).toUpperCase());
        String reqStr = JSON.toJSONString(payScanReq);
        PayResp payResp = JSON.parseObject(HttpClientUtils.doJsonPost(reqStr,apiUrl),PayResp.class);
        return payResp;
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

}
