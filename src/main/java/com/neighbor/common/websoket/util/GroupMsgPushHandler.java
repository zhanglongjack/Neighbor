package com.neighbor.common.websoket.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
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
import com.alibaba.fastjson.JSONObject;
import com.neighbor.app.api.common.SpringUtil;
import com.neighbor.app.group.entity.GroupMember;
import com.neighbor.app.group.service.GroupService;
import com.neighbor.app.packet.entity.Packet;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.common.util.DateUtils;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.websoket.constants.MessageStatus;
import com.neighbor.common.websoket.constants.WebSocketChatType;
import com.neighbor.common.websoket.constants.WebSocketMsgType;
import com.neighbor.common.websoket.handler.WebSocketMessageHandler;
import com.neighbor.common.websoket.po.GroupMsgRalation;
import com.neighbor.common.websoket.po.SocketMessage;
import com.neighbor.common.websoket.po.WebSocketHeader;
import com.neighbor.common.websoket.service.SocketMessageService;

@Component
public class GroupMsgPushHandler implements WebSocketHandler {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static final Map<Long, List<GroupMember>> userGroups = new ConcurrentHashMap<Long, List<GroupMember>>();
	private static final Map<Long, Map<Long, WebSocketSession>> groupSessions = new ConcurrentHashMap<Long, Map<Long, WebSocketSession>>();
	private static final Map<Long, WebSocketSession> allConnectUserSessions = new ConcurrentHashMap<Long, WebSocketSession>();
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
		allConnectUserSessions.put(user.getId(), session);
		List<GroupMember> groupList = groupService.selectUserOwnGroupsBy(user.getId());
		userGroups.put(user.getId(), groupList);
		logger.info("成功进入了系统。。。" + session.getAttributes().get("user"));
		Map<Long, List<SocketMessage>> groupsMsgList = new HashMap<Long, List<SocketMessage>>();
		for (GroupMember member : groupList) {
			List<SocketMessage> groupMsgList = socketMessageService.selectMsgByTargetGroupIdStatus(member.getGroupId(), member.getUserId());
			groupsMsgList.put(member.getGroupId(), groupMsgList); 
			
			Map<Long, WebSocketSession> groupUserSession = null;
			if (groupSessions.containsKey(member.getGroupId())) {
				groupUserSession = groupSessions.get(member.getGroupId());
			} else {
				groupUserSession = new HashMap<Long, WebSocketSession>();
			}

			groupUserSession.put(member.getUserId(), session);
			groupSessions.put(member.getGroupId(), groupUserSession);
			//
		}
		if(groupsMsgList.size()>0){
			pushGroupMsgToUser(groupsMsgList, user.getId());
		}
		
	}

	public void pushGroupMsgToUser(Map<Long, List<SocketMessage>> groupMsgList, Long userId) {

		ExecutorService fixedThreadPool = null;
		try {

			logger.info("userMsgListMap=== {}", groupMsgList);
			List<Long> userList = new ArrayList<Long>();
			userList.add(userId);
			
			fixedThreadPool = Executors.newFixedThreadPool(threadPoolSize);
			List<Future<Integer>> futureList = new ArrayList<Future<Integer>>();
			for (final Long key : groupMsgList.keySet()) {
				final List<SocketMessage> targetMsgList = groupMsgList.get(key);
				futureList.add(fixedThreadPool.submit(new Callable<Integer>() {
					@Override
					public Integer call() throws Exception {
						try {
							logger.info("给当前群[{}]成员[{}]推送消息数:{}", key, userId, targetMsgList.size());

							for (SocketMessage msgInfo : targetMsgList) {
								WebSocketMsgType msgType = WebSocketMsgType.valueOf(msgInfo.getMsgType());
								WebSocketChatType chatType = WebSocketChatType.valueOf(msgInfo.getChatType());
								WebSocketMessageHandler handler = (WebSocketMessageHandler) SpringUtil.getBean(msgType.getImplClass());
								ResponseResult handleResult = new ResponseResult(); // 消息已接收
								handleResult.setRequestID(msgInfo.getWebSocketHeader().getRequestId());
								handleResult.addBody("msgType", msgType);
								handleResult.addBody("chatType", chatType);
								handleResult.addBody("msgInfo", msgInfo);

								boolean isSend = sendMessageToUser(userId, key, handleResult);
								if (isSend) {
									msgInfo.setPushedUsers(userList);
									handler.successCallBack(msgInfo, chatType, msgType);
								} else {
									// 按顺序推送消息,有一条异常,后面则不推,可能是推送异常或者用户断开连接
									logger.info("按顺序推送消息,中断推送,可能是推送异常,用户未登录或者用户断开连接");
									break;
								}
							}

						} catch (Exception e) {
							logger.error("个人群[{}]成员[{}]推送消息异常", e);
							return -1;
						}
						return 1;
					}
				}));
			}

			for (Future<Integer> future : futureList) {
				try {
					future.get(1, TimeUnit.SECONDS);
				} catch (Exception e) {
					logger.error("消息结果获取异常", e);
				}
			}

		} catch (Exception e) {
			logger.error("处理一对一单人消息推送异常", e);
		} finally {
			if (fixedThreadPool != null) {
				fixedThreadPool.shutdown();
			}
		}
	}

	// public void sendPushedResponseMessage(List<SocketMessage> msgList) {
	//
	// for (SocketMessage msgInfo : msgList) {
	// WebSocketMsgType msgType =
	// WebSocketMsgType.valueOf(msgInfo.getMsgType());
	// WebSocketChatType chatType =
	// WebSocketChatType.valueOf(msgInfo.getChatType());
	//
	// WebSocketMessageHandler handler = (WebSocketMessageHandler)
	// SpringUtil.getBean(msgType.getImplClass());
	// ResponseResult handleResult = new ResponseResult(); // 消息已接收
	// handleResult.setRequestID(msgInfo.getWebSocketHeader().getRequestId());
	// handleResult.addBody("msgType", msgType);
	// handleResult.addBody("chatType", chatType);
	// handleResult.addBody("msgInfo", msgInfo);
	//
	// boolean isSend = sendMessageToUser(msgInfo.getTargetUserId(),
	// handleResult);
	// if (isSend) {
	// msgInfo.setStatus(MessageStatus.pushed + "");
	// handler.successCallBack(msgInfo, chatType, msgType);
	// } else {
	// // 按顺序推送消息,有一条异常,后面则不推,可能是推送异常或者用户断开连接
	// logger.info("按顺序推送消息,中断推送,可能是推送异常,用户未登录或者用户断开连接");
	// break;
	// }
	// }
	// }
	public static void main(String[] args) {
		System.out.println(WebSocketChatType.multiple+"");
		System.out.println(WebSocketMsgType.PACKET+"");
	}
	
	public void pushMessageToGroup(Packet packet){
		
		WebSocketHeader header = new WebSocketHeader();
		header.setRequestId(UUID.randomUUID().toString());
		header.setToken("token_system");
		header.setSign("sign_system");
		
		SocketMessage msgInfo = new SocketMessage();
		msgInfo.setRequestId(header.getRequestId());
		msgInfo.setContent(JSON.toJSONString(packet));
		msgInfo.setHeader(JSON.toJSONString(header));
		msgInfo.setStatus(MessageStatus.received + "");
		msgInfo.setChatType(WebSocketChatType.multiple+"");
		msgInfo.setMsgType(WebSocketMsgType.PACKET+"");
		msgInfo.setMasterMsgType("1");
		msgInfo.setTargetGroupId(packet.getGroupId());
		msgInfo.setSendUserId(packet.getUserId());
		msgInfo.setSendNickName(packet.getNickName());
		msgInfo.setDate(DateUtils.getStringDateShort());
		msgInfo.setTime(DateUtils.getTimeShort());
		
		WebSocketMessageHandler handler = (WebSocketMessageHandler) SpringUtil.getBean(WebSocketMsgType.PACKET.getImplClass());
		ResponseResult handleResult = handler.handleMessage(msgInfo, WebSocketChatType.multiple, WebSocketMsgType.PACKET); // 消息已接收
		handleResult.setRequestID(msgInfo.getWebSocketHeader().getRequestId());
		handleResult.addBody("msgType", WebSocketMsgType.PACKET);
		handleResult.addBody("chatType", WebSocketChatType.multiple);
		handleResult.addBody("msgInfo", msgInfo);
		
		GroupMsgRalation ralation = new GroupMsgRalation();
		ralation.setMsgId(msgInfo.getMsgId());
		ralation.setUserId(msgInfo.getSendUserId());
		ralation.setStatus(MessageStatus.complete+"");
		
		socketMessageService.insertRelationShipSelective(ralation);
		
		successHandleMsg(msgInfo, WebSocketMsgType.PACKET, WebSocketChatType.multiple, handler, handleResult);
		
	}
	
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		handleMessage(session, (String) message.getPayload());
	}

	
	
	private void handleMessage(WebSocketSession session, String message) {
		UserInfo user = (UserInfo) session.getAttributes().get("user");
		logger.info("收到群消息:" + message);
		JSONObject jsonObject = JSON.parseObject((String) message);
		if(jsonObject.containsKey("heartBeat") && jsonObject.getBooleanValue("heartBeat") ){
			logger.info("收到群消息用户[{}]的心跳消息",user.getId());
			return;
		}
		SocketMessage msgInfo = JSON.parseObject(message, SocketMessage.class);
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
				boolean isResponse = sendMessageToUser(msgInfo.getSendUserId(), msgInfo.getTargetGroupId(), handleResult); 
				GroupMsgRalation ralation = new GroupMsgRalation();
				ralation.setMsgId(msgInfo.getMsgId());
				ralation.setUserId(msgInfo.getSendUserId());
				ralation.setStatus(MessageStatus.complete+"");
				
				socketMessageService.insertRelationShipSelective(ralation);
				
				if("2".equals(msgInfo.getMasterMsgType()) && WebSocketMsgType.GROUP_ADD==msgType){
					WebSocketSession userSession = allConnectUserSessions.get(msgInfo.getTargetUserId());
					groupSessions.get(msgInfo.getTargetGroupId()).put(msgInfo.getTargetUserId(), userSession);
					if(!isResponse)isResponse=true;
				}else if("2".equals(msgInfo.getMasterMsgType()) && WebSocketMsgType.GROUP_QUIT==msgType){
					sendMessageToUser(msgInfo.getTargetUserId(), msgInfo.getTargetGroupId(), handleResult); 
					
					groupSessions.get(msgInfo.getTargetGroupId()).remove(msgInfo.getTargetUserId());
				}
				
				if (WebSocketChatType.multiple == chatType && isResponse) { 
					successHandleMsg(msgInfo, msgType, chatType, handler, handleResult);
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

	private void successHandleMsg(SocketMessage msgInfo, WebSocketMsgType msgType, WebSocketChatType chatType,
			WebSocketMessageHandler handler, ResponseResult handleResult) {
		logger.info("收到群发消息:" + msgInfo.getContent());
		// 消息消息发回,表示消息已接收
		msgInfo.setStatus(MessageStatus.pushed_response + "");
		// 消息推送
		List<Long> pushedUsers = sendMessagesToGroup(msgInfo.getSendUserId(), msgInfo.getTargetGroupId(),
				handleResult);
		// 成功推送的用户
		msgInfo.setPushedUsers(pushedUsers);
		handler.successCallBack(msgInfo, chatType, msgType);
	}

	// 后台错误信息处理方法
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		logger.error("消息处理异常", exception);
		UserInfo user = (UserInfo) session.getAttributes().get("user");
		if (session.isOpen()) {
			session.close();
		}
		userGroupExit(user.getId());
	}

	// 用户退出后的处理，不如退出之后，要将用户信息从websocket的session中remove掉，这样用户就处于离线状态了，也不会占用系统资源
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		UserInfo user = (UserInfo) session.getAttributes().get("user");
		if (session.isOpen()) {
			session.close();
		}
		if(user!=null){
			userGroupExit(user.getId());
		}

	}

	private void userGroupExit(Long userId) {
		
		List<GroupMember> groupList = userGroups.get(userId);
		for (GroupMember member : groupList) {
			groupSessions.get(member.getGroupId()).remove(userId);
		}

		logger.info("用户[{}]安全退出了群", userId);
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
		if(groupSession==null){
			return sendUserList;
		}
		ExecutorService fixedThreadPool = null;
		try {
			fixedThreadPool = Executors.newFixedThreadPool(threadPoolSize);
			List<Future<Long>> futureList = new ArrayList<Future<Long>>();
			Random r = new Random();
			for (final Long key : groupSession.keySet()) {
				futureList.add(fixedThreadPool.submit(new Callable<Long>() {
					@Override
					public Long call() throws Exception {
						long time = r.nextInt(100);
						logger.info("给当前群[{}]成员[{}]推送消息前睡眠时间:[{}]耗秒", key, userId,time);
						Thread.sleep(time);
						try {
							logger.info("给当前群[{}]成员[{}]推送消息", key, userId);
							if (key.equals(userId)) {
								return null;
							}

							boolean isSend = sendMessageToUser(key, groupId, result);
							return isSend ? key : null;
						} catch (Exception e) {
							logger.error("个人群[{}]成员[{}]推送消息异常", e);
							return null;
						}
					}
				}));
			}

			for (Future<Long> future : futureList) {
				try {
					Long groupMemberId = future.get(1, TimeUnit.SECONDS);
					if (groupMemberId != null) {
						sendUserList.add(groupMemberId);
					}
				} catch (Exception e) {
					logger.error("消息结果获取异常", e);
				}
			}

		} catch (Exception e) {
			logger.error("处理一对一单人消息推送异常", e);
		} finally {
			if (fixedThreadPool != null) {
				fixedThreadPool.shutdown();
			}
		}
		return sendUserList;
	}

	// /**
	// * 系统给所有的用户发送消息
	// *
	// * @return List<Long> : 成功推送的消息用户
	// */
	// public List<Long> sendSystemMessagesToUsers(Long userId, ResponseResult
	// result) {
	// logger.info("系统给所有用户发信息:" + result);
	// TextMessage message = new TextMessage(JSON.toJSONString(result));
	// List<Long> sendUserList = new ArrayList<Long>();
	// for (Long key : userSessions.keySet()) {
	// boolean isSend = sendMessage(userSessions.get(key), message);
	// if (isSend) {
	// sendUserList.add(key);
	// }
	// }
	//
	// return sendUserList;
	// }

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
	public boolean sendMessageToUser(Long userId, Long groupId, ResponseResult result) {
		logger.info("给指定群[{}]指定用户发[{}]信息:", groupId, userId);
		TextMessage message = new TextMessage(JSON.toJSONString(result));
		WebSocketSession session = groupSessions.get(groupId).get(userId);
		boolean isPushed = sendMessage(session, message);
		logger.info("给指定群[{}]指定用户:[{}]发信息是否成功:[{}]", groupId, userId, isPushed);
		return isPushed;
	}

	/**
	 * 新建群 或者 添加用户进群
	 *
	 * @return true:成功,false:失败
	 */
	public static boolean addGroupSessions(Long userId, Long groupId) {
		boolean b = false;
		if(allConnectUserSessions.containsKey(userId)){
			if(groupSessions.containsKey(groupId)){
				Map<Long,WebSocketSession> map = groupSessions.get(groupId);
				if(!map.containsKey(userId)){
					map.put(userId,allConnectUserSessions.get(userId));
					b=true;
				}
			}else{
				Map<Long,WebSocketSession> groupUserSession = new HashMap<Long, WebSocketSession>();
				groupUserSession.put(userId,allConnectUserSessions.get(userId));
				groupSessions.put(groupId,groupUserSession);
				b=true;
			}
		}
		return b;
	}

}