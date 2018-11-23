package com.neighbor.common.websoket.po;

import java.util.List;

import com.alibaba.fastjson.JSON;

public class SocketMessage {
	private String msgId;
	private String header;
	private String chatType;
	private String msgType;
	private Long sendUserId;
	private Long targetUserId;
	private Long targetGroupId;
	private String amount;
	private String number;
	private String hitAmountNumber;
	private String content;
	private String date;
	private String time;
	private String status; 
	public WebSocketHeader webSocketHeader;
	private List<Long> pushedUsers;
	
	
	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
		webSocketHeader = JSON.parseObject(header,WebSocketHeader.class);
	}

	public WebSocketHeader getWebSocketHeader() {
		return webSocketHeader;
	}

	public void setWebSocketHeader(WebSocketHeader webSocketHeader) {
		this.webSocketHeader = webSocketHeader;
	}

	public String getChatType() {
		return chatType;
	}

	public void setChatType(String chatType) {
		this.chatType = chatType;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public Long getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(Long sendUserId) {
		this.sendUserId = sendUserId;
	}

	public Long getTargetUserId() {
		return targetUserId;
	}

	public void setTargetUserId(Long targetUserId) {
		this.targetUserId = targetUserId;
	}

	public Long getTargetGroupId() {
		return targetGroupId;
	}

	public void setTargetGroupId(Long targetGroupId) {
		this.targetGroupId = targetGroupId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getHitAmountNumber() {
		return hitAmountNumber;
	}

	public void setHitAmountNumber(String hitAmountNumber) {
		this.hitAmountNumber = hitAmountNumber;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Long> getPushedUsers() {
		return pushedUsers;
	}

	public void setPushedUsers(List<Long> pushedUsers) {
		this.pushedUsers = pushedUsers;
	}

	@Override
	public String toString() {
		return String.format(
				"SocketMessage [msgId=%s, header=%s, chatType=%s, msgType=%s, sendUserId=%s, targetUserId=%s, targetGroupId=%s, amount=%s, number=%s, hitAmountNumber=%s, content=%s, date=%s, time=%s, status=%s, webSocketHeader=%s, pushedUsers=%s]",
				msgId, header, chatType, msgType, sendUserId, targetUserId, targetGroupId, amount, number,
				hitAmountNumber, content, date, time, status, webSocketHeader, pushedUsers);
	}
 

}
