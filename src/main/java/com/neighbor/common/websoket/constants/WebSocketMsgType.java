package com.neighbor.common.websoket.constants;

import com.neighbor.common.websoket.handler.impl.FailedMessageHandler;
import com.neighbor.common.websoket.handler.impl.ImageMessageHandler;
import com.neighbor.common.websoket.handler.impl.PacketMessageHandler;
import com.neighbor.common.websoket.handler.impl.ResponseMessageHandler;
import com.neighbor.common.websoket.handler.impl.SpeechMessageHandler;
import com.neighbor.common.websoket.handler.impl.TextMessageHandler;
import com.neighbor.common.websoket.handler.impl.TransferMessageHandler;
import com.neighbor.common.websoket.handler.impl.UnreadMessageHandler;
import com.neighbor.common.websoket.handler.impl.VideoMessageHandler;

public enum WebSocketMsgType {
	IMGR(ImageMessageHandler.class),
	PACKET(PacketMessageHandler.class),
	TEXT(TextMessageHandler.class),
	VIDEO(VideoMessageHandler.class),
	SPEECH(SpeechMessageHandler.class),
	TRANSFER(TransferMessageHandler.class),
	RESPONSE(ResponseMessageHandler.class),
//	RECEIVE(),
	FAILED(FailedMessageHandler.class),
	UNREAD(UnreadMessageHandler.class)
	;
	
	
	private Class<?> implClass;
	
	private WebSocketMsgType(Class<?> implClass) {
		this.implClass = implClass;
	}

	public Class<?> getImplClass() {
		return implClass;
	}


	
}
