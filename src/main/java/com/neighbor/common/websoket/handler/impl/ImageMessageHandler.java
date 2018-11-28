package com.neighbor.common.websoket.handler.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ImageMessageHandler  extends AbstractMessageHandler {
	private static final Logger logger = LoggerFactory.getLogger(ImageMessageHandler.class);

	@Override
	public ResponseResult handle() {
		// TODO Auto-generated method stub
		return null;
	}
 
}
