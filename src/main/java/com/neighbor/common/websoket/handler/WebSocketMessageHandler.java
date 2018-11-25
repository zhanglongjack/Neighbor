package com.neighbor.common.websoket.handler;

import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.websoket.constants.WebSocketChatType;
import com.neighbor.common.websoket.constants.WebSocketMsgType;
import com.neighbor.common.websoket.po.SocketMessage;

public interface WebSocketMessageHandler {
	public ResponseResult handleMessage(SocketMessage msgInfo, WebSocketChatType chatType, WebSocketMsgType msgType);

	public void successCallBack(SocketMessage msgInfo, WebSocketChatType chatType, WebSocketMsgType msgType);

	public void failedCallBack(SocketMessage msgInfo, WebSocketChatType chatType, WebSocketMsgType msgType);
}
