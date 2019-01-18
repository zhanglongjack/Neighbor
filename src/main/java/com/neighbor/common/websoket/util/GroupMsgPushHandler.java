package com.neighbor.common.websoket.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.alibaba.fastjson.JSON;
import com.neighbor.app.api.common.SpringUtil;
import com.neighbor.app.group.entity.GroupMember;
import com.neighbor.app.group.service.GroupService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.DateUtils;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.websoket.constants.MessageStatus;
import com.neighbor.common.websoket.constants.WebSocketChatType;
import com.neighbor.common.websoket.constants.WebSocketMsgType;
import com.neighbor.common.websoket.handler.WebSocketMessageHandler;
import com.neighbor.common.websoket.po.SocketMessage;
import com.neighbor.common.websoket.service.SocketMessageService;

@Component
public class GroupMsgPushHandler implements WebSocketHandler {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static final Map<Long, List<GroupMember>> userGroups = new ConcurrentHashMap<Long, List<GroupMember>>();
	private static final Map<Long, Map<Long, WebSocketSession>> groupSessions = new ConcurrentHashMap<Long, Map<Long, WebSocketSession>>();
	private static int threadPoolSize = 300;
	@Autowired
	private SocketMessageService socketMessageService;
	@Autowired
	private GroupService groupService;
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
		
		List<GroupMember> groupList = groupService.selectUserOwnGroupsBy(user.getId());
		userGroups.put(user.getId(), groupList);
		logger.info("成功进入了系统。。。" + session.getAttributes().get("user"));
		Map<Long,List<SocketMessage>> groupsMsgList = new HashMap<Long,List<SocketMessage>>();
		for(GroupMember member : groupList){
			List<SocketMessage> groupMsgList = socketMessageService.selectMsgByTargetGroupIdStatus(member.getGroupId(),member.getUserId());
			groupsMsgList.put(member.getUserId(), groupMsgList);
			
			Map<Long, WebSocketSession> groupUserSession = null;
			if(groupSessions.containsKey(member.getGroupId())){
				groupUserSession = groupSessions.get(member.getGroupId());
			}else{
				groupUserSession= new HashMap<Long, WebSocketSession>();
			}
			
			groupUserSession.put(member.getUserId(), session);
			groupSessions.put(member.getGroupId(), groupUserSession);
//			
		}
		
		pushGroupMsgToUser(groupsMsgList,user.getId());
	}

	public void pushGroupMsgToUser(Map<Long,List<SocketMessage>> groupMsgList,Long userId) {
		
		ExecutorService fixedThreadPool = null;
		try {
			
			logger.info("userMsgListMap=== {}",groupMsgList);
			
			fixedThreadPool = Executors.newFixedThreadPool(threadPoolSize);
			List<Future<Integer>> futureList = new ArrayList<Future<Integer>>();
			for (final Long key : groupMsgList.keySet()) {
				final List<SocketMessage> targetMsgList = groupMsgList.get(key);
				futureList.add(fixedThreadPool.submit(new Callable<Integer>() {
					@Override
					public Integer call() throws Exception {
						try {
							logger.info("给当前群[{}]成员[{}]推送消息数:{}",key,userId,targetMsgList.size());
							
							for (SocketMessage msgInfo : targetMsgList) {
								WebSocketMsgType msgType = WebSocketMsgType.valueOf(msgInfo.getMsgType());
								WebSocketChatType chatType = WebSocketChatType.valueOf(msgInfo.getChatType());
								WebSocketMessageHandler handler = (WebSocketMessageHandler) SpringUtil.getBean(msgType.getImplClass());
								ResponseResult handleResult = new ResponseResult(); // 消息已接收
								handleResult.setRequestID(msgInfo.getWebSocketHeader().getRequestId());
								handleResult.addBody("msgType", msgType);
								handleResult.addBody("chatType", chatType);
								handleResult.addBody("msgInfo", msgInfo);

								boolean isSend = sendMessageToUser(msgInfo.getTargetUserId(),key, handleResult);
								if (isSend) {
									handler.successCallBack(msgInfo, chatType, msgType);
								} else {
									// 按顺序推送消息,有一条异常,后面则不推,可能是推送异常或者用户断开连接
									logger.info("按顺序推送消息,中断推送,可能是推送异常,用户未登录或者用户断开连接");
									break;
								}
							}
							
						} catch (Exception e) {
							logger.error("个人群[{}]成员[{}]推送消息异常",e);
							return -1;
						}
						return 1;
					}
				}));
			}
			
			for(Future<Integer> future : futureList){
				try {
					future.get(1, TimeUnit.SECONDS);
				} catch (Exception e) {
					logger.error("消息结果获取异常",e);
				}
			}
			
		} catch (Exception e) {
			logger.error("处理一对一单人消息推送异常",e);
		}finally{
			if(fixedThreadPool!=null){
				fixedThreadPool.shutdown();
			}
		}
	}
	
//	public void sendPushedResponseMessage(List<SocketMessage> msgList) {
//		
//		for (SocketMessage msgInfo : msgList) {
//			WebSocketMsgType msgType = WebSocketMsgType.valueOf(msgInfo.getMsgType());
//			WebSocketChatType chatType = WebSocketChatType.valueOf(msgInfo.getChatType());
//
//			WebSocketMessageHandler handler = (WebSocketMessageHandler) SpringUtil.getBean(msgType.getImplClass());
//			ResponseResult handleResult = new ResponseResult(); // 消息已接收
//			handleResult.setRequestID(msgInfo.getWebSocketHeader().getRequestId());
//			handleResult.addBody("msgType", msgType);
//			handleResult.addBody("chatType", chatType);
//			handleResult.addBody("msgInfo", msgInfo);
//
//			boolean isSend = sendMessageToUser(msgInfo.getTargetUserId(), handleResult);
//			if (isSend) {
//				msgInfo.setStatus(MessageStatus.pushed + "");
//				handler.successCallBack(msgInfo, chatType, msgType);
//			} else {
//				// 按顺序推送消息,有一条异常,后面则不推,可能是推送异常或者用户断开连接
//				logger.info("按顺序推送消息,中断推送,可能是推送异常,用户未登录或者用户断开连接");
//				break;
//			}
//		}
//	}

	//
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		logger.info("收到消息:" + message.getPayload());

		SocketMessage msgInfo = JSON.parseObject((String) message.getPayload(), SocketMessage.class);
		msgInfo.setStatus(MessageStatus.received + "");
		msgInfo.setDate(DateUtils.getStringDateShort());
		msgInfo.setTime(DateUtils.getTimeShort());
		msgInfo.setRequestId(msgInfo.getWebSocketHeader().getRequestId());

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
				 boolean isResponse = sendMessageToUser(msgInfo.getSendUserId(),msgInfo.getTargetGroupId(), handleResult);
				 if (WebSocketChatType.multiple == chatType && isResponse) {
					logger.info("收到群发消息:" + msgInfo.getContent());
					// 消息消息发回,表示消息已接收
					msgInfo.setStatus(MessageStatus.pushed_response + "");
					// 消息推送
					List<Long> pushedUsers = sendMessagesToGroup(msgInfo.getSendUserId(), msgInfo.getTargetGroupId(),
							handleResult);
					// 成功推送的用户
					msgInfo.setPushedUsers(pushedUsers);
					handler.successCallBack(msgInfo, chatType, msgType);

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

	// 后台错误信息处理方法
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		logger.error("消息处理异常", exception);
		if (session.isOpen()) {
			session.close();
		}
		userGroupExit(session);
	}

	// 用户退出后的处理，不如退出之后，要将用户信息从websocket的session中remove掉，这样用户就处于离线状态了，也不会占用系统资源
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		if (session.isOpen()) {
			session.close();
		}
		userGroupExit(session);

	}

	private void userGroupExit(WebSocketSession session) {
		UserInfo user = (UserInfo) session.getAttributes().get("user");
		List<GroupMember> groupList = userGroups.get(user.getId());
		for(GroupMember member : groupList ){
			groupSessions.get(member.getGroupId()).remove(user.getId());
		}
		
		logger.info("用户[{}]安全退出了群",user.getId());
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	/**
	 * 单条消息群发
	 *
	 * @return
	 */
	public List<Long> sendMessagesToGroup(Long userId, Long groupId, ResponseResult result) {
		logger.info("单条消息群发:" + result);
		Map<Long, WebSocketSession> groupSession = groupSessions.get(groupId);
		List<Long> sendUserList = new ArrayList<Long>();

		ExecutorService fixedThreadPool = null;
		try {
			fixedThreadPool = Executors.newFixedThreadPool(threadPoolSize);
			List<Future<Long>> futureList = new ArrayList<Future<Long>>();
			for (final Long key : groupSession.keySet()) {
				futureList.add(fixedThreadPool.submit(new Callable<Long>() {
					@Override
					public Long call() throws Exception {
						try {
							logger.info("给当前群[{}]成员[{}]推送消息",key,userId);
							if (key.equals(userId)) {
								return key;
							}
							
							boolean isSend = sendMessageToUser(key,groupId, result);
							return isSend?key:null;
						} catch (Exception e) {
							logger.error("个人群[{}]成员[{}]推送消息异常",e);
							return null;
						}
					}
				}));
			}
			
			for(Future<Long> future : futureList){
				try {
					Long groupMemberId = future.get(1, TimeUnit.SECONDS);
					if(groupMemberId!=null){
						sendUserList.add(groupMemberId);
					}
				} catch (Exception e) {
					logger.error("消息结果获取异常",e);
				}
			}
			
		} catch (Exception e) {
			logger.error("处理一对一单人消息推送异常",e);
		}finally{
			if(fixedThreadPool!=null){
				fixedThreadPool.shutdown();
			}
		}
		return sendUserList;
	}

//	/**
//	 * 系统给所有的用户发送消息
//	 *
//	 * @return List<Long> : 成功推送的消息用户
//	 */
//	public List<Long> sendSystemMessagesToUsers(Long userId, ResponseResult result) {
//		logger.info("系统给所有用户发信息:" + result);
//		TextMessage message = new TextMessage(JSON.toJSONString(result));
//		List<Long> sendUserList = new ArrayList<Long>();
//		for (Long key : userSessions.keySet()) {
//			boolean isSend = sendMessage(userSessions.get(key), message);
//			if (isSend) {
//				sendUserList.add(key);
//			}
//		}
//
//		return sendUserList;
//	}

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
	public boolean sendMessageToUser(Long userId,Long groupId, ResponseResult result) {
		logger.info("给指定群[{}]指定用户发[{}]信息:" ,groupId, userId);
		TextMessage message = new TextMessage(JSON.toJSONString(result));
		WebSocketSession session = groupSessions.get(groupId).get(userId);
		boolean isPushed = sendMessage(session, message);
		logger.info("给指定群[{}]指定用户:[{}]发信息是否成功:[{}]",groupId, userId, isPushed);
		return isPushed;
	}
}