package com.neighbor.app.robot.util;

import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.neighbor.app.robot.entity.GrapPacketData;
import com.neighbor.app.robot.service.RobotConfigService;

@Component 
public class GrapPacketConsumer implements ApplicationRunner,Runnable {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private BlockingQueue<GrapPacketData> robotQueue;
	@Autowired
	private RobotConfigService robotConfigService;
	
	private final static int ROBOT_GRAP_SLEEP_SECONDS = 4;// 机器人抢包休眠时间
	private final static int ROBOT_GRAP_QUEUE_HANDLE_SIZE = 30;// 机器人抢包休眠时间
	private static boolean isRunning = true;
//	@SuppressWarnings("unchecked")
//	public Consumer() {
//		this.queue = (BlockingQueue<GrapPacketData>) SpringUtil.getBean("robotQueue");
//		robotConfigService = SpringUtil.getBean(RobotConfigService.class);
//	}

	public void run() {
		logger.info("启动机器人抢红包队列消费者线程！");
		try {
			while (isRunning) {
				try {
					logger.info("开始取队列数据");
					GrapPacketData data = robotQueue.take();
					logger.info("队列当前堆积数:{},检查是否获得队列数据:{}",robotQueue.size(),data);
					if (null != data) {
						int seconds = robotQueue.size()>ROBOT_GRAP_QUEUE_HANDLE_SIZE?1:ROBOT_GRAP_SLEEP_SECONDS;
						robotConfigService.robotGrapPacket(data,seconds);
					}
				} catch (Exception e) {
					logger.error("机器队列消费异常",e);
				}
				
			}
		} catch (Exception e) {
			logger.error("机器队列消费异常",e);
			Thread.currentThread().interrupt();
		} finally{
			isRunning = false;
		}
		
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		new Thread(this).start();
	}
	


}