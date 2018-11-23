package com.neighbor.common.websoket.po;

public class WebSocketHeader {
	private String requestId;
	private String token;
	private String sign;
	
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Override
	public String toString() {
		return String.format("WebSocketHeader [requestId=%s, token=%s, sign=%s]", requestId, token, sign);
	}
	
}
