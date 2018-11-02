package com.neighbor.common.sms;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;

public class TencentSms {
	// 短信应用SDK AppID
	public static final int appid = 1400151898; // 1400开头

	// 短信应用SDK AppKey
	public static final String appkey = "605364543d5f88d4ba6c164c4376b304";

	// 需要发送短信的手机号码
	public static final String[] phoneNumbers = {"15999585921", "12345678902", "12345678903"};

	// 短信模板ID，需要在短信应用中申请
	public static final int templateId = 7839; // NOTE: 这里的模板ID`7839`只是一个示例，真实的模板ID需要在短信控制台中申请
	//templateId7839对应的内容是"您的验证码是: {1}"
	// 签名
	public static final String smsSign = "腾讯云"; // NOTE: 这里的签名"腾讯云"只是一个示例，真实的签名需要在短信控制台中申请，另外签名参数使用的是`签名内容`，而不是`签名ID`
	/**
	 * 缓存短信验证码,后期优化可以自动删除过期验证码
	 */
	public static final Map<String,String> smsCache = new HashMap<String,String>();
	
	public static void smsSend(String code,String phone){
		smsCache.put(phone, code);
		try {
		    String[] params = {code};//数组具体的元素个数和模板中变量个数必须一致，例如事例中templateId:5678对应一个变量，参数数组中元素个数也必须是一个
		    SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
		    SmsSingleSenderResult result = ssender.sendWithParam("86", phone,
		        templateId, params, smsSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
		    System.out.println(result);
		} catch (HTTPException e) {
		    // HTTP响应码错误
		    e.printStackTrace();
		} catch (JSONException e) {
		    // json解析错误
		    e.printStackTrace();
		} catch (IOException e) {
		    // 网络IO错误
		    e.printStackTrace();
		}
	}
}
