package com.neighbor.common.websoket.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.alibaba.fastjson.JSON;
import com.neighbor.app.api.common.SpringUtil;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.DateUtils;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.websoket.constants.MessageStatus;
import com.neighbor.common.websoket.constants.WebSocketChatType;
import com.neighbor.common.websoket.constants.WebSocketMsgType;
import com.neighbor.common.websoket.handler.WebSocketMessageHandler;
import com.neighbor.common.websoket.po.SocketMessage;

public class WebSocketPushHandler implements WebSocketHandler {
	private static final Logger logger = LoggerFactory.getLogger(WebSocketPushHandler.class);
	private static final Map<Long, WebSocketSession> userSessions = new ConcurrentHashMap<Long, WebSocketSession>();
	private static final Map<Long, Map<Long, WebSocketSession>> groupSessions = new ConcurrentHashMap<Long, Map<Long, WebSocketSession>>();

	// 用户进入系统监听
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {

		UserInfo user = (UserInfo) session.getAttributes().get("user");
		if (user == null) {
			ResponseResult result = new ResponseResult();
			result.setErrorCode(3);
			result.setErrorMessage("用户未登录");

			TextMessage message = new TextMessage(JSON.toJSONString(result));
			session.sendMessage(message);
			// handleTransportError(session, null);
			return;
		}

		logger.info("成功进入了系统。。。" + session.getAttributes().get("user"));
		userSessions.put(user.getId(), session);
	}

	//
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		logger.info("收到消息:" + message.getPayload());
		SocketMessage msgInfo = JSON.parseObject((String) message.getPayload(), SocketMessage.class);
		msgInfo.setStatus(MessageStatus.received + "");
		msgInfo.setDate(DateUtils.getStringDateShort());
		msgInfo.setTime(DateUtils.getTimeShort());
		
		WebSocketMsgType msgType = WebSocketMsgType.valueOf(msgInfo.getMsgType());
		WebSocketChatType chatType = WebSocketChatType.valueOf(msgInfo.getChatType());

		WebSocketMessageHandler handler = (WebSocketMessageHandler) SpringUtil.getBean(msgType.getImplClass());
		ResponseResult handleResult = handler.handleMessage(msgInfo); // 消息已接收
		handleResult.setRequestID(msgInfo.getWebSocketHeader().getRequestId());
		handleResult.addBody("msgType", msgType);
		handleResult.addBody("chatType", chatType);
		handleResult.addBody("msgInfo", msgInfo);
		
		boolean isSend = sendMessageToUser(msgInfo.getSendUserId(), handleResult);
		if (WebSocketChatType.single == chatType && isSend) {
			logger.info("收到单发消息:" + msgInfo.getContent());
			// 消息消息发回,表示消息已接收
			msgInfo.setStatus(MessageStatus.pushed_response + "");
			handler.successCallBack(msgInfo);
			// 消息推送
			isSend = sendMessageToUser(msgInfo.getTargetUserId(), handleResult);
			if (isSend) {
				msgInfo.setStatus(MessageStatus.pushed + "");
				handler.successCallBack(msgInfo);
			}

			// sendResponseMessage(msgInfo.getSendUserId(),result);
		} else if(WebSocketChatType.multiple == chatType && isSend){
			logger.info("收到群发消息:" + msgInfo.getContent());
			// 消息消息发回,表示消息已接收
			msgInfo.setStatus(MessageStatus.pushed_response + "");
			handler.successCallBack(msgInfo);
			// 消息推送
			List<Long> pushedUsers = sendMessagesToGroup(msgInfo.getSendUserId(), msgInfo.getTargetGroupId(), handleResult);
			// 成功推送的用户
			msgInfo.setPushedUsers(pushedUsers);
			handler.successCallBack(msgInfo);
			
		}else{
			msgInfo.setStatus(MessageStatus.push_failed+"");
			handler.failedCallBack(msgInfo);
		}
		logger.info("消息处理结束" +msgInfo);
	}

	// /**
	// * 发送成功消息
	// * @param sendUserId
	// * @param result
	// */
	// private void sendResponseMessage(Long sendUserId,ResponseResult result) {
	// logger.info("发送成功消息userId={},result={}",sendUserId,result);
	// result.addBody("msgType", WebSocketMsgType.RESPONSE);
	//
	// sendMessageToUser(sendUserId,result);
	// }

	// 后台错误信息处理方法
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		logger.error("消息处理异常", exception);
		if (session.isOpen()) {
			session.close();
		}
		userSessions.remove(session);
	}

	// 用户退出后的处理，不如退出之后，要将用户信息从websocket的session中remove掉，这样用户就处于离线状态了，也不会占用系统资源
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		if (session.isOpen()) {
			session.close();
		}
		userSessions.remove(session);
		logger.info("安全退出了系统");

	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	/**
	 * 给指定群发消息
	 * 
	 * @return
	 */
	public List<Long> sendMessagesToGroup(Long userId, Long groupId, ResponseResult result) {
		logger.info("给指定群发消息:" + result);
		TextMessage message = new TextMessage(JSON.toJSONString(result));
		Map<Long, WebSocketSession> groupSession = groupSessions.get(groupId);
		List<Long> sendUserList = new ArrayList<Long>();
		for (Long key : groupSession.keySet()) {
			WebSocketSession userSession = groupSession.get(key);
			UserInfo user = (UserInfo) userSession.getAttributes().get("user");
			if(user.getId().equals(userId)){
				sendUserList.add(user.getId());
				continue;
			}
				
			boolean isSend = sendMessage(userSession, message);
			if (isSend) {
				sendUserList.add(user.getId());
			}
		}

		return sendUserList;
	}

	/**
	 * 系统给所有的用户发送消息
	 * 
	 * @return List<Long> : 成功推送的消息用户
	 */
	public List<Long> sendSystemMessagesToUsers(Long userId, ResponseResult result) {
		logger.info("系统给所有用户发信息:" + result);
		TextMessage message = new TextMessage(JSON.toJSONString(result));
		List<Long> sendUserList = new ArrayList<Long>();
		for (Long key : userSessions.keySet()) {
			boolean isSend = sendMessage(userSessions.get(key), message);
			if (isSend) {
				sendUserList.add(key);
			}
		}

		return sendUserList;
	}

	private boolean sendMessage(WebSocketSession session, TextMessage message) {
		if(session==null){
			return false;
		}
		try {
			// isOpen()在线就发送
			if (session.isOpen()) {
				session.sendMessage(message);
				return true;
			}
		} catch (IOException e) {
			logger.error("消息推送异常", e);
		}
		return false;
	}

	/**
	 * 发送消息给指定的用户
	 * 
	 * @return true:成功,false:失败
	 */
	public boolean sendMessageToUser(Long userId, ResponseResult result) {
		logger.info("给指定用户发信息:" + userId);
		TextMessage message = new TextMessage(JSON.toJSONString(result));
		WebSocketSession session = userSessions.get(userId);
		return sendMessage(session, message);
	}
}
