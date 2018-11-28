package com.neighbor.common.websoket.handler.impl;

import java.io.IOException;

import com.neighbor.common.util.FileUploadUtil;
import com.neighbor.common.websoket.service.SocketMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.alibaba.fastjson.JSON;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.websoket.constants.WebSocketChatType;
import com.neighbor.common.websoket.constants.WebSocketMsgType;
import com.neighbor.common.websoket.handler.WebSocketMessageHandler;
import com.neighbor.common.websoket.po.SocketMessage;

@Component
public class ImageMessageHandler implements WebSocketMessageHandler {
	private static final Logger logger = LoggerFactory.getLogger(ImageMessageHandler.class);
	@Autowired
	private SocketMessageService socketMessageService;
	@Override
	public ResponseResult handleMessage(SocketMessage msgInfo, WebSocketChatType chatType, WebSocketMsgType msgType) {
		logger.info("开始处理图片消息接收处理:{}",msgInfo);
		String filepath = FileUploadUtil.chatImagePath(msgInfo.getSendUserId(),msgInfo.getTargetUserId());
		msgInfo.setContent(filepath+msgInfo.getContent());
		socketMessageService.insertSelective(msgInfo);
		ResponseResult result = new ResponseResult();
		return result;
	}

	@Override
	public void successCallBack(SocketMessage msgInfo, WebSocketChatType chatType, WebSocketMsgType msgType) {
		// TODO Auto-generated method stub
		logger.info("成功,更新消息状态:{}",msgInfo);
		socketMessageService.updateByPrimaryKeySelective(msgInfo);
	}

	@Override
	public void failedCallBack(SocketMessage msgInfo, WebSocketChatType chatType, WebSocketMsgType msgType) {
		// TODO Auto-generated method stub
		logger.info("消息推送失败,更新消息状态:{}",msgInfo);
		socketMessageService.updateByPrimaryKeySelective(msgInfo);
	}
 
}
