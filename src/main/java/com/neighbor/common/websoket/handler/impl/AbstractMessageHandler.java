package com.neighbor.common.websoket.handler.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.neighbor.app.friend.entity.Friend;
import com.neighbor.app.friend.service.FriendService;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.websoket.constants.WebSocketChatType;
import com.neighbor.common.websoket.constants.WebSocketMsgType;
import com.neighbor.common.websoket.handler.WebSocketMessageHandler;
import com.neighbor.common.websoket.po.SocketMessage;
import com.neighbor.common.websoket.service.SocketMessageService;

public abstract class AbstractMessageHandler implements WebSocketMessageHandler{
	private static final Logger logger = LoggerFactory.getLogger(AbstractMessageHandler.class);
	
	@Autowired
	private SocketMessageService socketMessageService;
	@Autowired
	private FriendService friendService;
	@Override
	public ResponseResult handleMessage(SocketMessage msgInfo, WebSocketChatType chatType, WebSocketMsgType msgType) {
		logger.info("开始处理通用消息接收处理:{}",msgInfo);
		Friend friend = new Friend();
		friend.setUserId(msgInfo.getSendUserId());
		friend.setFriendUserId(msgInfo.getTargetUserId());
		ResponseResult handleResult = new ResponseResult();
		
		try {
			Friend resultFriend = friendService.viewFriendByUserIdAndFriendId(friend);
			if(resultFriend.getStates()=="2"){
				handleResult.setErrorCode(4);
				handleResult.setErrorMessage("双方非互为好友,消息发送失败");
				return handleResult;
			}
		} catch (Exception e) {
			
		}
		
		socketMessageService.insertSelective(msgInfo);
		return handleResult;
	}

	@Override
	public void successCallBack(SocketMessage msgInfo, WebSocketChatType chatType, WebSocketMsgType msgType) {
		logger.info("成功,更新消息状态:{}",msgInfo);
		handle();
		socketMessageService.updateByPrimaryKeySelective(msgInfo);
		Map<String,Long> relationShip = new HashMap<String,Long>();
		relationShip.put("msgId", msgInfo.getMsgId());
		
		List<Long> userIds = msgInfo.getPushedUsers();
		if(chatType == WebSocketChatType.multiple && userIds!=null){
			for(Long userId : userIds){
				relationShip.put("userId", userId);
				socketMessageService.insertRelationShipSelective(relationShip);
			}
		}
		
		
	}

	@Override
	public void failedCallBack(SocketMessage msgInfo, WebSocketChatType chatType, WebSocketMsgType msgType) {
		logger.info("消息推送失败,更新消息状态:{}",msgInfo);
		socketMessageService.updateByPrimaryKeySelective(msgInfo);
	}
	
	public abstract ResponseResult handle(); 
}
