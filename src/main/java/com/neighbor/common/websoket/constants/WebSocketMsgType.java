package com.neighbor.common.websoket.constants;

import com.neighbor.common.websoket.handler.impl.*;

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
	UNREAD(UnreadMessageHandler.class),
	FRIEND_CONFIRM(FriendConfirmMessageHandler.class),
	FRIEND_ADD(TextMessageHandler.class),
	GROUP_ADD(TextMessageHandler.class);
	private Class<?> implClass;
	
	private WebSocketMsgType(Class<?> implClass) {
		this.implClass = implClass;
	}

	public Class<?> getImplClass() {
		return implClass;
	}


	
}
