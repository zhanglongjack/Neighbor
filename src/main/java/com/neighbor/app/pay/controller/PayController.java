package com.neighbor.app.pay.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.neighbor.app.pay.entity.NotifyResp;
import com.neighbor.app.recharge.entity.Recharge;
import com.neighbor.app.recharge.service.RechargeService;
import com.neighbor.common.websoket.service.SocketMessageService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/pay")
@SessionAttributes("user")
public class PayController {

    private final static Logger logger = LoggerFactory.getLogger(PayController.class);
    @Autowired
    private Environment env;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private SocketMessageService socketMessageService;

    @RequestMapping(value = "/notify")
    @ResponseBody
    public String payNotify() throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String reqUrl = request.getRequestURL().toString();
        logger.info("\n\n\n");
        logger.info("------支付结果---异步通知----------开始----------");
        logger.info("reqUrl : "+reqUrl);
        //打印header
        logger.info("--------------------------------------------------------");
        logger.info("requestHeaders : "+ JSONObject.toJSONString(getRequestHeader(request)));
        logger.info("--------------------------------------------------------");
        //打印param
        String requestParams = JSONObject.toJSONString(getRequestParams(request));
        logger.info("requestParams : "+requestParams);
        logger.info("--------------------------------------------------------");
        //打印body
        String reqBody = null;
        if("post".equalsIgnoreCase(request.getMethod())){
            InputStream inputStream = request.getInputStream();
            reqBody = IOUtils.toString(inputStream, "utf-8");
            logger.info("reqBody : "+reqBody );
            String appid = env.getProperty("recharge.app.id");
            NotifyResp notifyResp = JSON.parseObject(reqBody,NotifyResp.class);
            //支付成功 退款暂时不做
            if(notifyResp.getAppid().equals(appid)&&"1".equals(notifyResp.getStatus())){
                Long uId = rechargeService.payNotify(notifyResp);
                if(uId!=null&&uId>0){
                    //通知更新用户钱包
                    socketMessageService.walletRefreshNotice(null, uId, "系统通知");
                }
            }

        }
        logger.info("------支付结果---异步通知----------结束----------");
        logger.info("\n\n\n");
        return "SUCCESS";
    }

    public  Map<String, String> getRequestParams(HttpServletRequest req) {
        Map<String, String> requestParams = new HashMap<String, String>();
        Map<String, String[]> paramters = req.getParameterMap();
        if (paramters != null && !paramters.isEmpty()) {
            for (String paramterName : paramters.keySet()) {
                String paramValue = paramters.get(paramterName)[0].toString();
                requestParams.put(paramterName, paramValue);
            }
        }
        return requestParams;
    }

    public Map<String, String> getRequestHeader(HttpServletRequest req) {
        Map<String, String> requestHeaders = new HashMap<String, String>();
        Enumeration enumeration = req.getHeaderNames();
        while (enumeration.hasMoreElements()){
            Object key = enumeration.nextElement();
            if(key!=null){
                String keyStr = key.toString();
                requestHeaders.put(keyStr,req.getHeader(keyStr));
            }
        }
        return requestHeaders;
    }




}