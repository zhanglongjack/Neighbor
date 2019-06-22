package com.neighbor.common.sms;

public interface SMSSender {
	public void sendSMS(String phone,String templateId,String contentParams[]);
	
	public void smsSend(String code ,String phone);
	
	public void removeSMSCode(String phone);
}
