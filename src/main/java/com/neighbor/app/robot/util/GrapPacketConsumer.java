package com.neighbor.app.robot.util;

import java.util.concurrent.BlockingQueue;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.neighbor.app.robot.entity.GrapPacketData;
import com.neighbor.app.robot.service.RobotConfigService;
import com.neighbor.common.constants.CommonConstants;
import com.neighbor.common.constants.EnvConstants;

@Component 
public class GrapPacketConsumer implements ApplicationRunner,Runnable {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private BlockingQueue<GrapPacketData> robotQueue;
	@Autowired
	private RobotConfigService robotConfigService;
    @Autowired
    private CommonConstants commonConstants;
    
	private static boolean isRunning; 
	
	public void run() {
		logger.info("启动机器人抢红包队列消费者线程！");
		try {
			while (isRunning) {
				GrapPacketData data = null;
				try {
					logger.info("开始取队列数据");
					data = robotQueue.take();
					logger.info("队列当前堆积数:{},检查是否获得队列数据:{}",robotQueue.size(),data);
				} catch (InterruptedException e) {
					logger.error("机器队列消费异常,休眠5秒后再开始尝试取数据",e);
					try {
						Thread.sleep(5000);
					} catch (Exception e1) {}
				}
				
				try {
					if (null != data) {
						String handleSize = commonConstants.getDictionarysBy(EnvConstants.PACKET_CONF, EnvConstants.ROBOT_GRAP_QUEUE_HANDLE_SIZE);
						String sleepSeconds = commonConstants.getDictionarysBy(EnvConstants.PACKET_CONF, EnvConstants.ROBOT_GRAP_SLEEP_SECONDS);

						int seconds = robotQueue.size()>Integer.parseInt(handleSize)?1:Integer.parseInt(sleepSeconds);
						robotConfigService.robotGrapPacket(data,seconds);
					}
				} catch (Exception e) {
					logger.error("机器人队列消费抢红包处理异常",e);
				}
			}
		} finally{
			isRunning = false;
		}
		
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		start();
	}
	
	public void stop(){
		isRunning = false;
	}
	
	public void start(){
		isRunning = true;
		new Thread(this).start();
	}

	@PreDestroy
	public void destroy(){
		stop();
	}

}