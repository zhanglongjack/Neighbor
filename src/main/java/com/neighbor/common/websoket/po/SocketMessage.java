package com.neighbor.common.websoket.po;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neighbor.common.util.PageTools;

public class SocketMessage {
	private Long msgId;
	private String requestId;
	private String header;
	private String masterMsgType;
	private String chatType;
	private String msgType;
	private Long sendUserId;
	private String sendNickName;
	private Long targetUserId;
	private Long targetGroupId;
	private String bizId;
	private String content;
	private String date;
	private String time;
	private String status; 
	
	@JsonIgnore
	private PageTools pageTools;
	
	@JsonIgnore
	private WebSocketHeader webSocketHeader;
	private List<Long> pushedUsers;
	
	// 查询条件
	private Integer targetUserNotNull;
	private String dateLess;
	private String timeLess;
	
	
//	private Long msgIdLess;

	private String cleanHistory;//清除历史消息间隔时间

	private String sendUserDeleteFlag; //发送者删除状态（0 正常，1被删除了）
	private String targetUserDeleteFlag;//接受者删除状态（0 正常，1被删除了）

	public String getSendUserDeleteFlag() {
		return sendUserDeleteFlag;
	}

	public void setSendUserDeleteFlag(String sendUserDeleteFlag) {
		this.sendUserDeleteFlag = sendUserDeleteFlag;
	}

	public String getTargetUserDeleteFlag() {
		return targetUserDeleteFlag;
	}

	public void setTargetUserDeleteFlag(String targetUserDeleteFlag) {
		this.targetUserDeleteFlag = targetUserDeleteFlag;
	}

	public String getCleanHistory() {
		return cleanHistory;
	}

	public void setCleanHistory(String cleanHistory) {
		this.cleanHistory = cleanHistory;
	}

	public Long getMsgId() {
		return msgId;
	}

	public void setMsgId(Long msgId) {
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

	public String getMasterMsgType() {
		return masterMsgType;
	}

	public void setMasterMsgType(String masterMsgType) {
		this.masterMsgType = masterMsgType;
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

	public String getSendNickName() {
		return sendNickName;
	}

	public void setSendNickName(String sendNickName) {
		this.sendNickName = sendNickName;
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

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public Integer getTargetUserNotNull() {
		return targetUserNotNull;
	}

	public void setTargetUserNotNull(Integer targetUserNotNull) {
		this.targetUserNotNull = targetUserNotNull;
	}

	public String getDateLess() {
		return dateLess;
	}

	public void setDateLess(String dateLess) {
		this.dateLess = dateLess;
	}

	public String getTimeLess() {
		return timeLess;
	}

	public void setTimeLess(String timeLess) {
		this.timeLess = timeLess;
	}

	public PageTools getPageTools() {
		return pageTools;
	}

	public void setPageTools(PageTools pageTools) {
		this.pageTools = pageTools;
	}

	@Override
	public String toString() {
		return String.format(
				"SocketMessage [msgId=%s, header=%s, chatType=%s, msgType=%s, sendUserId=%s, targetUserId=%s, targetGroupId=%s, content=%s, date=%s, time=%s, status=%s, webSocketHeader=%s, pushedUsers=%s]",
				msgId, header, chatType, msgType, sendUserId, targetUserId, targetGroupId, content, date, time, status,
				webSocketHeader, pushedUsers);
	}


 

}
