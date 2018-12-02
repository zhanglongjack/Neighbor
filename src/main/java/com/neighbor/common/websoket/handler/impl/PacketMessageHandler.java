package com.neighbor.common.websoket.handler.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.neighbor.app.packet.entity.Packet;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.websoket.constants.WebSocketChatType;
import com.neighbor.common.websoket.constants.WebSocketMsgType;
import com.neighbor.common.websoket.po.SocketMessage;

@Component
public class PacketMessageHandler extends AbstractMessageHandler {
	private static final Logger logger = LoggerFactory.getLogger(PacketMessageHandler.class);
	
	@Override
	public ResponseResult handleMessage(SocketMessage msgInfo, WebSocketChatType chatType, WebSocketMsgType msgType) {
		logger.info("开始处理红包类消息");
		Packet packet = JSON.parseObject(msgInfo.getContent(),Packet.class);
		msgInfo.setBizId(packet.getId()+"");
		return super.handleMessage(msgInfo, chatType, msgType);
	}
	
	@Override
	public ResponseResult handle() {
		// TODO Auto-generated method stub
		return null;
	}
 
 
}
