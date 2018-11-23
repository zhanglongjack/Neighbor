package com.neighbor.common.websoket.handler.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.alibaba.fastjson.JSON;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.websoket.handler.WebSocketMessageHandler;
import com.neighbor.common.websoket.po.SocketMessage;

@Component
public class ResponseMessageHandler implements WebSocketMessageHandler {
	private static final Logger logger = LoggerFactory.getLogger(ResponseMessageHandler.class);
	@Override
	public ResponseResult handleMessage(SocketMessage msgInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void successCallBack(SocketMessage msgInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void failedCallBack(SocketMessage msgInfo) {
		// TODO Auto-generated method stub
		
	}
}
