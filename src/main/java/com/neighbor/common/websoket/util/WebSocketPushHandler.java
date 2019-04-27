package com.neighbor.common.websoket.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.neighbor.app.api.common.SpringUtil;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.DateUtils;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.websoket.constants.MessageStatus;
import com.neighbor.common.websoket.constants.WebSocketChatType;
import com.neighbor.common.websoket.constants.WebSocketMsgType;
import com.neighbor.common.websoket.handler.WebSocketMessageHandler;
import com.neighbor.common.websoket.po.SocketMessage;
import com.neighbor.common.websoket.service.SocketMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketPushHandler implements WebSocketHandler {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static final Map<Long, WebSocketSession> userSessions = new ConcurrentHashMap<Long, WebSocketSession>();
	//private static final Map<Long, Map<Long, WebSocketSession>> groupSessions = new ConcurrentHashMap<Long, Map<Long, WebSocketSession>>();
	
	@Autowired
	private SocketMessageService socketMessageService;

	public Map<Long, WebSocketSession> getUsersessions() {
		return userSessions;
	}

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
		List<SocketMessage> msgList = socketMessageService.selectByTargetUserIdStatus(user.getId(),
				MessageStatus.pushed_response + "", WebSocketChatType.single + "");
		sendPushedResponseMessage(msgList);

	}

	public void sendPushedResponseMessage(List<SocketMessage> msgList) {
		for (SocketMessage msgInfo : msgList) {
			WebSocketMsgType msgType = WebSocketMsgType.valueOf(msgInfo.getMsgType());
			WebSocketChatType chatType = WebSocketChatType.valueOf(msgInfo.getChatType());

			WebSocketMessageHandler handler = (WebSocketMessageHandler) SpringUtil.getBean(msgType.getImplClass());
			ResponseResult handleResult = new ResponseResult(); // 消息已接收
			handleResult.setRequestID(msgInfo.getWebSocketHeader().getRequestId());
			handleResult.addBody("msgType", msgType);
			handleResult.addBody("chatType", chatType);
			handleResult.addBody("msgInfo", msgInfo);

			boolean isSend = sendMessageToUser(msgInfo.getTargetUserId(), handleResult);
			if (isSend) {
				msgInfo.setStatus(MessageStatus.pushed + "");
				handler.successCallBack(msgInfo, chatType, msgType);
			} else {
				// 按顺序推送消息,有一条异常,后面则不推,可能是推送异常或者用户断开连接
				logger.info("按顺序推送消息,中断推送,可能是推送异常,用户未登录或者用户断开连接");
				break;
			}
		}
	}

	//
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		UserInfo user = (UserInfo) session.getAttributes().get("user");
		logger.info("收到单聊消息:" + message.getPayload());
		JSONObject jsonObject = JSON.parseObject((String) message.getPayload());
		if(jsonObject.containsKey("heartBeat") && jsonObject.getBooleanValue("heartBeat") ){
			logger.info("收到单聊用户[{}]的心跳消息",user.getId());
			return;
		}
		SocketMessage msgInfo = JSON.parseObject((String) message.getPayload(), SocketMessage.class);
		msgInfo.setStatus(MessageStatus.received + "");
		msgInfo.setDate(DateUtils.getStringDateShort());
		msgInfo.setTime(DateUtils.getTimeShort());
		msgInfo.setRequestId(msgInfo.getWebSocketHeader().getRequestId());
		msgInfo.setSendNickName(user.getNickName());
		
		WebSocketMsgType msgType = WebSocketMsgType.valueOf(msgInfo.getMsgType());
		WebSocketChatType chatType = WebSocketChatType.valueOf(msgInfo.getChatType());
		WebSocketMessageHandler handler = (WebSocketMessageHandler) SpringUtil.getBean(msgType.getImplClass());
		try {
			ResponseResult handleResult = handler.handleMessage(msgInfo, chatType, msgType); // 消息已接收
			handleResult.setRequestID(msgInfo.getWebSocketHeader().getRequestId());
			handleResult.addBody("msgType", msgType);
			handleResult.addBody("chatType", chatType);
			handleResult.addBody("msgInfo", msgInfo);
			if (handleResult.getErrorCode() == 0) {
				boolean isResponse = sendMessageToUser(msgInfo.getSendUserId(), handleResult);
				if (WebSocketChatType.single == chatType && isResponse) {
					logger.info("收到单发消息:" + msgInfo.getContent());
					// 消息消息发回,表示消息已接收
					msgInfo.setStatus(MessageStatus.pushed_response + "");
					// 消息推送
					boolean isSend = sendMessageToUser(msgInfo.getTargetUserId(), handleResult);
					if (isSend) {
						msgInfo.setStatus(MessageStatus.pushed + "");
					}

					handler.successCallBack(msgInfo, chatType, msgType);
					// sendResponseMessage(msgInfo.getSendUserId(),result);
				} else if (WebSocketChatType.multiple == chatType && isResponse) {
//					logger.info("收到群发消息:" + msgInfo.getContent());
//					// 消息消息发回,表示消息已接收
//					msgInfo.setStatus(MessageStatus.pushed_response + "");
//					// 消息推送
//					List<Long> pushedUsers = sendMessagesToGroup(msgInfo.getSendUserId(), msgInfo.getTargetGroupId(),
//							handleResult);
//					// 成功推送的用户
//					msgInfo.setPushedUsers(pushedUsers);
//					handler.successCallBack(msgInfo, chatType, msgType);

				} else {
					msgInfo.setStatus(MessageStatus.response_failed + "");
					handler.failedCallBack(msgInfo, chatType, msgType);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			handler.failedCallBack(msgInfo, chatType, msgType);
		}
		logger.info("消息处理结束" + msgInfo);
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

//	/**
//	 * 给指定群发消息
//	 *
//	 * @return
//	 */
//	public List<Long> sendMessagesToGroup(Long userId, Long groupId, ResponseResult result) {
//		logger.info("给指定群发消息:" + result);
//		TextMessage message = new TextMessage(JSON.toJSONString(result));
//		Map<Long, WebSocketSession> groupSession = groupSessions.get(groupId);
//		List<Long> sendUserList = new ArrayList<Long>();
//		for (Long key : groupSession.keySet()) {
//			WebSocketSession userSession = groupSession.get(key);
//			UserInfo user = (UserInfo) userSession.getAttributes().get("user");
//			if (user.getId().equals(userId)) {
//				sendUserList.add(user.getId());
//				continue;
//			}
//
//			boolean isSend = sendMessage(userSession, message);
//			if (isSend) {
//				sendUserList.add(user.getId());
//			}
//		}
//
//		return sendUserList;
//	}

	/**
	 * 系统给所有的用户发送消息
	 *
	 * @return List<Long> : 成功推送的消息用户
	 */
	public List<Long> sendSystemMessagesToUsers(ResponseResult result) {
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
		if (session == null) {
			logger.info("用户断开连接或退出登录或未登录");
			return false;
		}
		try {
			// isOpen()在线就发送
			if (session.isOpen()) {
				session.sendMessage(message);
				return true;
			}
		} catch (Exception e) {
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
		boolean isPushed = sendMessage(session, message);
		logger.info("给指定用户:[{}]发信息是否成功:[{}]", userId, isPushed);
		return isPushed;
	}

	public void forceUserOffline(ResponseResult handleResult) {
		List<Long> sendUserList = sendSystemMessagesToUsers(handleResult);
		
		for(Long userId : sendUserList){
			WebSocketSession session = userSessions.get(userId);
			if(session!=null){
				userSessions.remove(userId);
				try {
					session.close();
				} catch (Exception e) {
					logger.error("强制中断用户连接异常:"+userId,e);
				}
			}
		}
	}

}