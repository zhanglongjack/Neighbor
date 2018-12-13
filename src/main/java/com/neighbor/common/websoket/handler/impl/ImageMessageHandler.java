package com.neighbor.common.websoket.handler.impl;

import com.neighbor.common.util.FileUploadUtil;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.websoket.constants.WebSocketChatType;
import com.neighbor.common.websoket.constants.WebSocketMsgType;
import com.neighbor.common.websoket.handler.WebSocketMessageHandler;
import com.neighbor.common.websoket.po.SocketMessage;
import com.neighbor.common.websoket.service.SocketMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImageMessageHandler extends AbstractMessageHandler {
//	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public ResponseResult handle() {
		return null;
	}


}
