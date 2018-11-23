package com.neighbor.common.util;

import java.util.HashMap;
import java.util.Map;

import com.neighbor.app.api.common.ErrorCodeDesc;

public class ResponseResult {
	private Map<String, Object> body = new HashMap<String, Object>();

	private int errorCode = ErrorCodeDesc.success.getValue();
	private String errorMessage = null;
	private String serviceURL = null;
	private String requestID = null;

	public Map<String, Object> getBody() {
		return body;
	}

	public void addBody(String key, Object value) {
		this.body.put(key, value);
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getServiceURL() {
		return serviceURL;
	}

	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

}
