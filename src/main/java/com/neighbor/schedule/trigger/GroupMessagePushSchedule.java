package com.neighbor.schedule.trigger;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.neighbor.common.util.DateUtils;
import com.neighbor.common.websoket.constants.WebSocketChatType;
import com.neighbor.common.websoket.po.SocketMessage;
import com.neighbor.common.websoket.service.SocketMessageService;

@Component
public class GroupMessagePushSchedule {
	private static final Logger logger = LoggerFactory.getLogger(GroupMessagePushSchedule.class);
	@Autowired
	private SocketMessageService socketMessageService;
	
	@Scheduled(cron = "0 0/1 * * * *")
	public void userMsgPush() {
		logger.info("开始处理过期消息任务");
		SocketMessage msgParams = new SocketMessage();
		msgParams.setChatType(WebSocketChatType.multiple.toString());
		msgParams.setDateLess(DateUtils.getStringDateShort());
		handle(msgParams);
		
		msgParams.setDateLess(null);
		msgParams.setDate(DateUtils.getStringDateShort());
		msgParams.setTimeLess(DateUtils.getTimeBy(-3));
		handle(msgParams);
		
	}

	public void handle(SocketMessage msgParams) {
		List<SocketMessage> msgList = socketMessageService.selectbySelective(msgParams);
		if(msgList.size()>0){
			logger.info("开始备份过期消息数据并删除主表过期消息");
			socketMessageService.insertMsgBackup(msgList.get(msgList.size()-1));
		}
	}
}
