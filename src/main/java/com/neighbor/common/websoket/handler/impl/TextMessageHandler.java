package com.neighbor.common.websoket.handler.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.websoket.constants.WebSocketChatType;
import com.neighbor.common.websoket.constants.WebSocketMsgType;
import com.neighbor.common.websoket.handler.WebSocketMessageHandler;
import com.neighbor.common.websoket.po.SocketMessage;
import com.neighbor.common.websoket.service.SocketMessageService;

@Component
public class TextMessageHandler implements WebSocketMessageHandler {
	
	@Autowired
	private SocketMessageService socketMessageService;
	
	private static final Logger logger = LoggerFactory.getLogger(TextMessageHandler.class);
	@Override
	public ResponseResult handleMessage(SocketMessage msgInfo, WebSocketChatType chatType, WebSocketMsgType msgType) {
		logger.info("开始处理文字类消息接收处理:{}",msgInfo);

		socketMessageService.insertSelective(msgInfo);
		
		ResponseResult result = new ResponseResult();
		return result;
	}

	@Override
	public void successCallBack(SocketMessage msgInfo, WebSocketChatType chatType, WebSocketMsgType msgType) {
		logger.info("成功,更新消息状态:{}",msgInfo);
		socketMessageService.updateByPrimaryKeySelective(msgInfo);
//		if(msgInfo.getChatType())
		
		
		
	}

	@Override
	public void failedCallBack(SocketMessage msgInfo, WebSocketChatType chatType, WebSocketMsgType msgType) {
		logger.info("消息推送失败,更新消息状态:{}",msgInfo);
		socketMessageService.updateByPrimaryKeySelective(msgInfo);
	}

}
