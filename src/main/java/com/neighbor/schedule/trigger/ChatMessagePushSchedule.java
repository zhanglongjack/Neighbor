package com.neighbor.schedule.trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.neighbor.common.websoket.constants.MessageStatus;
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
	private static int threadPoolSize = 300;

	/**
	 * 个人消息定时推送任务
	 */
	@Scheduled(cron = "0/30 * * * * *")
	public void userMsgPush() {
		ExecutorService fixedThreadPool = null;
		// logger.info("每30秒执行一次定时任务推送消息");
		try {
			logger.info("开始多线程推送一对一消息,以用户为一个维度");
			List<SocketMessage> allTargetMsgList = socketMessageService.selectForTargetUserMsgByStatus(MessageStatus.pushed_response+"");
			Map<Long, List<SocketMessage>> userMsgListMap = new HashMap<Long, List<SocketMessage>>();
			for (SocketMessage sm : allTargetMsgList) {
				if (userMsgListMap.containsKey(sm.getTargetUserId())) {
					userMsgListMap.get(sm.getTargetUserId()).add(sm);
				} else if (!userMsgListMap.containsKey(sm.getTargetUserId())) {
					List<SocketMessage> msgList = new ArrayList<>();
					msgList.add(sm);
					userMsgListMap.put(sm.getTargetUserId(), msgList);
				}
			}
			
			logger.info("userMsgListMap=== {}",userMsgListMap);
			
			fixedThreadPool = Executors.newFixedThreadPool(threadPoolSize);
			List<Future<Integer>> futureList = new ArrayList<Future<Integer>>();
			for (Long key : userMsgListMap.keySet()) {
				final List<SocketMessage> targetMsgList = userMsgListMap.get(key);
				futureList.add(fixedThreadPool.submit(new Callable<Integer>() {
					@Override
					public Integer call() throws Exception {
						try {
							logger.info("给用户[{}]推送消息数:{}",key,targetMsgList.size());
							webSocketPushHandler.sendPushedResponseMessage(targetMsgList);
						} catch (Exception e) {
							logger.error("消息定时推送异常",e);
							return -1;
						}
						return 1;
					}
				}));
			}
			
			for(Future<Integer> future : futureList){
				try {
					future.get(1, TimeUnit.SECONDS);
				} catch (Exception e) {
					logger.error("消息结果获取异常",e);
				}
			}
			
		} catch (Exception e) {
			logger.error("处理一对一单人消息推送异常",e);
		}finally{
			if(fixedThreadPool!=null){
				fixedThreadPool.shutdown();
			}
		}
	}
}
