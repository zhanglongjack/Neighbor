package com.neighbor.app.commission.util;

import java.util.concurrent.BlockingQueue;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.neighbor.app.commission.entity.CommissionHandleTask;
import com.neighbor.app.commission.service.CommissionHandleTaskService;

@Component
public class CommissionHandleTaskConsumer implements ApplicationRunner, Runnable {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private BlockingQueue<CommissionHandleTask> commisionHandleTaskQueue;
	@Autowired
	private CommissionHandleTaskService commissionHandleTaskService;

	private static boolean isRunning = true;

	public void run() {
		logger.info("启动分佣处理消费者线程！");
		while (isRunning) {
			try {
				logger.info("开始取分佣任务数据");
				CommissionHandleTask data = commisionHandleTaskQueue.take();
				logger.info("检查是否获得分佣队列数据:{}", data);
				if (null != data) {
					commissionHandleTaskService.handleSplitCommission(data);
				}
			} catch (Exception e) {
				logger.error("分佣任务消费异常", e);
			}
		}
	}

	@PreDestroy
	public void destroy() {
		logger.info("程序关闭,停止消费分佣任务");
		isRunning = false;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		new Thread(this).start();
	}

}