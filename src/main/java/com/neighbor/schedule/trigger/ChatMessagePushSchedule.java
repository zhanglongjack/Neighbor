package com.neighbor.schedule.trigger;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.neighbor.common.websoket.po.SocketMessage;
import com.neighbor.common.websoket.service.SocketMessageService;
import com.neighbor.common.websoket.util.WebSocketPushHandler;

@Component
public class ChatMessagePushSchedule {
	private static final Logger logger = LoggerFactory.getLogger(ChatMessagePushSchedule.class);
	@Autowired
	private WebSocketPushHandler webSocketPushHandler;
	@Autowired
	private SocketMessageService socketMessageService;
	
	@Scheduled(cron="0/30 * * * * *") 
    public void breakCheck() {
		//logger.info("每30秒执行一次定时任务推送消息");
		List<SocketMessage> msgList = socketMessageService.selectByStatus("pushed_response");
		webSocketPushHandler.sendPushedResponseMessage(msgList);
    }
}
