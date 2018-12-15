package com.neighbor.schedule.trigger;

import com.neighbor.app.chatlist.service.ChatListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CleanChatMessageSchedule {
	private static final Logger logger = LoggerFactory.getLogger(CleanChatMessageSchedule.class);
	@Autowired
	private ChatListService chatListService;

	@Scheduled(cron="0 0 */1 * * ?")
    public void transferBackJob() {
		//logger.info("每1小时秒执行一次定时任务清理好友聊天记录");
		try {
			logger.info(">>> 执行好友聊天记录清理");
			chatListService.jobDeleteChatHistory();
			logger.info("<<< 执行好友聊天记录清理JOB运行结束");
		} catch (Exception e) {
			logger.info("执行清理好友聊天记录出现异常！",e);
		}
	}
}
