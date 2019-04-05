package com.neighbor.common.websoket.handler.impl;

import java.util.List;
import com.neighbor.common.websoket.constants.MessageDeleteStates;
import com.neighbor.common.websoket.constants.MessageStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.neighbor.app.friend.entity.Friend;
import com.neighbor.app.friend.service.FriendService;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.websoket.constants.WebSocketChatType;
import com.neighbor.common.websoket.constants.WebSocketMsgType;
import com.neighbor.common.websoket.handler.WebSocketMessageHandler;
import com.neighbor.common.websoket.po.GroupMsgRalation;
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
		ResponseResult handleResult = new ResponseResult();
		if(chatType == WebSocketChatType.single){
			if("1".equals(msgInfo.getMasterMsgType())){//消息类型需要判断 通知类型无需是好友也可发送（例如客服审核线下充值通知用户更新钱包）
				Friend friend = new Friend();
				friend.setUserId(msgInfo.getSendUserId());
				friend.setFriendUserId(msgInfo.getTargetUserId());
				try {
					Friend resultFriend = friendService.viewFriendByUserIdAndFriendId(friend);
					if(resultFriend.getStates()=="2"){
						handleResult.setErrorCode(4);
						handleResult.setErrorMessage("双方非互为好友,消息发送失败");
						return handleResult;
					}
				} catch (Exception e) {

				}
			}
			msgInfo.setTargetUserDeleteFlag(MessageDeleteStates.normal.getDes());
			msgInfo.setSendUserDeleteFlag(MessageDeleteStates.normal.getDes());
		}
		
		socketMessageService.insertSelective(msgInfo);
		return handleResult;
	}

	@Override
	public void successCallBack(SocketMessage msgInfo, WebSocketChatType chatType, WebSocketMsgType msgType) {
		logger.info("成功,更新消息状态:{}",msgInfo);
		handle();
		socketMessageService.updateByPrimaryKeySelective(msgInfo);

		List<Long> userIds = msgInfo.getPushedUsers();
		if(chatType == WebSocketChatType.multiple && userIds!=null){
			for(Long userId : userIds){
				GroupMsgRalation ralation = new GroupMsgRalation();
				ralation.setMsgId(msgInfo.getMsgId());
				ralation.setUserId(userId);
				ralation.setStatus(MessageStatus.received+"");
				socketMessageService.insertRelationShipSelective(ralation);
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