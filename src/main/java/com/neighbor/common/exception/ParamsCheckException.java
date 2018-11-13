package com.neighbor.common.exception;

public class ParamsCheckException extends Exception{
	private static final long serialVersionUID = 1L;
	private int errorCode;
	private String errorMessage;
    public ParamsCheckException(int errorCode,String errorMessage) {
    	super(errorMessage);
    	this.errorCode = errorCode;
    	this.errorMessage = errorMessage;
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
    
}
