package com.neighbor.common.websoket.handler.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.websoket.handler.WebSocketMessageHandler;
import com.neighbor.common.websoket.po.SocketMessage;

@Component
public class VideoMessageHandler implements WebSocketMessageHandler {
	private static final Logger logger = LoggerFactory.getLogger(VideoMessageHandler.class);
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
