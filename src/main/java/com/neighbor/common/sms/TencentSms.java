package com.neighbor.common.sms;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;

public class TencentSms {
	private static final Logger logger = LoggerFactory.getLogger(TencentSms.class);
	
	// 短信应用SDK AppID
	public static final int appid = 1400158495; // 1400开头

	// 短信应用SDK AppKey
	public static final String appkey = "67439111bb0c3c9d27eb39aba86038ce";

	// 短信模板ID，需要在短信应用中申请
	public static final int templateId = 265289; // NOTE: 这里的模板ID`7839`只是一个示例，真实的模板ID需要在短信控制台中申请
	//templateId7839对应的内容是"您的验证码是: {1}"
	// 签名
	public static final String smsSign = "绿色的心情"; // NOTE: 这里的签名"腾讯云"只是一个示例，真实的签名需要在短信控制台中申请，另外签名参数使用的是`签名内容`，而不是`签名ID`
	/**
	 * 缓存短信验证码,后期优化可以自动删除过期验证码
	 */
	public static final Map<String,String> smsCache = new HashMap<String,String>();
	public static String createVerifyCode(){
		return  (int)(Math.random()*100000)+"";
	}
	private static SmsSingleSender  ssender=null;
	static{
		checkSender();
	}
	
	private static synchronized void checkSender(){
		if(ssender==null){
			try {
				ssender = new SmsSingleSender(appid, appkey);
			} catch (Exception e) {
				logger.error("创建短信发送工具异常",e);
			}
		}
	}
	
	public static void smsSend(String code ,String phone){
		if(code==null){
			smsCache.put(phone, "123456");
			return;
		}
		smsCache.put(phone, code);
		try {
			checkSender();
		    String[] params = {code ,"5"};//"【好邻居】" 数组具体的元素个数和模板中变量个数必须一致，例如事例中templateId:5678对应一个变量，参数数组中元素个数也必须是一个
		    logger.info("短信发送参数:"+params);
		    SmsSingleSenderResult result = ssender.sendWithParam("86", phone,
		        templateId, params, smsSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
		    logger.info(result+"");
		} catch (HTTPException e) {
		    logger.error("HTTP响应码错误",e);
		} catch (JSONException e) {
			logger.error("json解析错误",e);
		} catch (IOException e) {
			logger.error("网络IO错误",e);
		}
	}
	
	public static void main(String[] args) {
		TencentSms.smsSend("123456", "15999585921");
		System.err.println("第一条发送完成");
//		TencentSms.smsSend("666666", "15999585921");
//		System.err.println("第二条发送完成");
	}
}
