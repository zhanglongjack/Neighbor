package com.neighbor.schedule.trigger;

import com.neighbor.app.transfer.service.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TransferBackSchedule {
	private static final Logger logger = LoggerFactory.getLogger(TransferBackSchedule.class);
	@Autowired
	private TransferService transferService;

	@Scheduled(cron="0 0 */1 * * ?")
    public void transferBackJob() {
		//logger.info("每1小时秒执行一次定时任务转账退回");
		try {
			logger.info(">>> 执行转账待退回JOB运行中");
			transferService.transferBackJob();
			logger.info("<<< 执行转账待退回JOB运行结束");
		} catch (Exception e) {
			logger.info("执行查询待退回转账记录出现异常！",e);
		}
	}
}
