package com.neighbor.common.websoket.handler;

import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.websoket.po.SocketMessage;

public interface WebSocketMessageHandler {
	public ResponseResult handleMessage(SocketMessage msgInfo);

	public void successCallBack(SocketMessage msgInfo);

	public void failedCallBack(SocketMessage msgInfo);
}
