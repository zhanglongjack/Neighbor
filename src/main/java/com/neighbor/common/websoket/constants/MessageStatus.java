package com.neighbor.common.websoket.constants;

public enum MessageStatus {
	received("已接收"),
	pushed_response("已推送应答"),
	pushed("已推送消息"),
	push_failed("消息推送失败"),
	response_failed("应答推送失败"),
	complete("完成");

	private String desc;

	private MessageStatus(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
	
}
