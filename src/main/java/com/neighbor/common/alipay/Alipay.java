package com.neighbor.common.alipay;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
//import com.aspire.boc.util.ResourceManager;
//import com.google.gson.Gson;
//import com.qlwb.business.payment.vo.AlipayVo;

/**
 *  * 支付宝账号向用户转账工具类（单例）  * @author Administrator  *  
 */
public class Alipay {

//	// 关联配置文件
//	private static ResourceManager rm = ResourceManager.getInstance();
//
//	private static String gateway = rm.getValue("gateway");// 支付宝网关
//	private static String appid = rm.getValue("appid");// 阿里公共账户的id
//	private static String private_key = rm.getValue("private_key");// 私钥
//	private static String input_charset = rm.getValue("input_charset");// 字段类型
//	private static String ali_public_key = rm.getValue("ali_public_key");// 公钥
//
//	private static AlipayClient alipayClient;
//	private static Alipay instance = new Alipay();
//
//	private Alipay() {
//		alipayClient = new DefaultAlipayClient(gateway, appid, private_key, "json", input_charset, ali_public_key,
//				"RSA2");
//	}
//
//	public static Alipay getInstance() {
//		return instance;
//	}
}
