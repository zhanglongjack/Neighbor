package com.neighbor.common.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        httppost.setHeader("Accept","application/json");
        httppost.setHeader("Content-Type","application/json");
        httppost.setHeader("Accept-Charset","UTF-8");
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

    /**
     * http post请求传参的方法 返回实体
     */
    public static String httpPostWithPAaram(String url, Map<String, String> params) throws Exception {
        String result = null;
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();//设置请求和传输超时时间
        // 创建httppost
        HttpPost httppost = new HttpPost(url);
        httppost.setConfig(requestConfig);
        try {
            logger.info("http reqStr ： "+ JSON.toJSONString(params));
            List<BasicNameValuePair> formparams = buildNameValuePair(params);
            if (null != formparams) {
                UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(
                        formparams, "UTF-8");
                httppost.setEntity(uefEntity);
            }
            response = httpclient.execute(httppost);
            result = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
            logger.info("http result ： "+result);
            return result;
        } catch (Exception e) {
            logger.error("remote post exception");
        }
        return result;
    }

    /**
     * MAP类型数组转换成BasicNameValuePair类型
     */
    public static List<BasicNameValuePair> buildNameValuePair(Map<String, String> params) {
        List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        return nvps;
    }


    public static void main(String[] args) {
        HttpClientUtils.doGet("https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?cardNo=&cardBinCheck=true");
    }
}

