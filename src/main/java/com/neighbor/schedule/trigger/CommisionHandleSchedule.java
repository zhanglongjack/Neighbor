package com.neighbor.schedule.trigger;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.neighbor.app.commission.entity.CommissionHandleTask;
import com.neighbor.app.commission.service.CommissionHandleTaskService;

@Component
public class CommisionHandleSchedule {
	private static final Logger logger = LoggerFactory.getLogger(CommisionHandleSchedule.class);
	@Autowired
	private CommissionHandleTaskService commissionHandleTaskService;
	
	private static boolean isRunning = false;
	@Scheduled(cron = "0 0/1 * * * *")
	public void handle() {
		try {
			logger.info("开始处理分佣处理定时任务"); 
			if(!isRunning){
				isRunning = true;
				
				List<CommissionHandleTask> taskList = commissionHandleTaskService.selectTaskByStatus(0);
				for(CommissionHandleTask data : taskList){
					try {
						commissionHandleTaskService.handleSplitCommission(data);
					} catch (Exception e) {
						logger.error("分佣定时任务处理异常",e);
					}
				}
			}
		} catch (Exception e) {
			logger.error("分佣定时任务查询处理异常:"+e.getMessage(),e);
		}finally{
			isRunning = false;
			logger.info("结束处理分佣处理定时任务"); 
		}
	}

}
