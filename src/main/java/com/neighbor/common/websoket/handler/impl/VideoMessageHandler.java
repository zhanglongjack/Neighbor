package com.neighbor.common.websoket.handler.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.websoket.constants.WebSocketChatType;
import com.neighbor.common.websoket.constants.WebSocketMsgType;
import com.neighbor.common.websoket.handler.WebSocketMessageHandler;
import com.neighbor.common.websoket.po.SocketMessage;

@Component
public class VideoMessageHandler  extends AbstractMessageHandler {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public ResponseResult handle() {
		// TODO Auto-generated method stub
		return null;
	}
 
}
