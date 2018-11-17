package com.neighbor.common.util;

import com.neighbor.app.bankcard.controller.BankCardController;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * 
 */
public class HttpClientUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
    public static String doJsonPost(String reqStr,String host){
       logger.info("request host >>> "+host);
       logger.info("reqStr >>> "+reqStr);
        String result = null;
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();//设置请求和传输超时时间
        // 创建httppost
        HttpPost httppost = new HttpPost(host);
        httppost.setConfig(requestConfig);
        try {
            StringEntity s = new StringEntity(reqStr);
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");//发送json数据需要设置contentType
            httppost.setEntity(s);
            response = httpclient.execute(httppost);
            result = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))
                    .lines().collect(Collectors.joining("\n"));
           logger.info("respStr <<< " + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
           logger.info("remote post exception ---- " + e.getMessage());
        }
        return result;
    }

    public static String doGet(String reqUrl){
        logger.info("reqUrl >>> "+reqUrl);
        String result = null;
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();//设置请求和传输超时时间
        // 创建httppost
        HttpGet httpGet = new HttpGet(reqUrl);
        httpGet.setConfig(requestConfig);
        try {
            response = httpclient.execute(httpGet);
            result = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))
                    .lines().collect(Collectors.joining("\n"));
            logger.info("respStr <<< " + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("remote get exception ---- " + e.getMessage());
        }
        return result;
    }


    public static void main(String[] args) {
        HttpClientUtils.doGet("https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?cardNo=6225887858108521&cardBinCheck=true");
    }
}

