package com.neighbor.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.neighbor.app.users.constants.UserContainer;

public abstract class ExcelMappingsAbstract {
	private static final Logger logger = LoggerFactory.getLogger(ExcelMappingsAbstract.class);
	
	protected String sheetName;
	@Autowired
	private UserContainer userContainer;
	
	protected JSONArray data;
	
	public static Map<String,Map<Integer,String>> dictionaries = new HashMap<String, Map<Integer,String>>();
	
	static{
//		dictionaries.put("paymentMethod", PaymentMethodStatus.paymentMethodMap);
//		dictionaries.put("orderStatus", OrderStatus.orderStatusMap); 
//		dictionaries.put("consumeType", ConsumeType.consumeTypeMap); 
	}
	
	public String getColumnsMappingValue(String dictionarieName,String key){
//		logger.debug("dictionarieName:{},key:{}",dictionarieName,key);
		if(dictionaries.containsKey(dictionarieName)){
			return dictionaries.get(dictionarieName).get(Integer.parseInt(key));
		}else if("userId".equals(dictionarieName)){
//			logger.debug("userMap:{},",userContainer.userMap);
			return userContainer.userMap.get(Long.parseLong(key));
		} 
		return key;
	}
	
	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public abstract List<String> getHeaderList();

	public abstract List<String> getColumnsMappings();

	public JSONArray getData() {
		return data;
	}

	public void setData(JSONArray data) {
		this.data = data;
	}

//	public abstract String getColumnsMappingValue(String dictionaries, String key);
	
	
	

}
