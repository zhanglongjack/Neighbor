package com.neighbor.schedule.trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import com.neighbor.common.websoket.constants.MessageStatus;
import com.neighbor.common.websoket.constants.WebSocketChatType;
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
	private static int threadPoolSize = Runtime.getRuntime().availableProcessors() + 1;
	private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(threadPoolSize);
	private static BlockingQueue<Long> userQueue = new LinkedBlockingQueue<Long>(threadPoolSize);
	private static boolean runFlag = true;
	private static boolean isRunning = true;
	
	@PostConstruct
	public void init(){
		fixedThreadPool.execute(new Runnable() {
			public void run() {
				logger.info("开始个人消息推送任务队列消费！");
				try {
					while (isRunning) {
						try {
							logger.info("开始取个人推送消息的队列数据");
							Long userId = userQueue.take();
							logger.info("检查是否获得个人推送消息的队列数据:{}",userId);
							
							fixedThreadPool.execute(new Runnable() {
								@Override
								public void run() {
									try {
										List<SocketMessage> targetMsgList = socketMessageService.selectByTargetUserIdStatus(userId,
												MessageStatus.pushed_response + "", WebSocketChatType.single + "");
										logger.info("给用户[{}]推送消息数:{}", userId, targetMsgList.size());
										webSocketPushHandler.sendPushedResponseMessage(targetMsgList);
									} catch (Exception e) {
										logger.error("个人消息定时推送异常,用户编号:"+userId, e);
									}
								}
							});
							
						} catch (Exception e) {
							logger.error("个人消息推送任务添加失败",e);
						}
						
					}
				} catch (Exception e) {
					logger.error("机器队列消费异常",e);
					Thread.currentThread().interrupt();
				} finally{
					isRunning = false;
				}
			}
		});
	}
	
	@PreDestroy
	public void destroy() {
		logger.info("个人消息定时推送任务线程池准备销毁");
		if (fixedThreadPool != null) {
			fixedThreadPool.shutdown();
		}
	}

	/**
	 * 个人消息定时推送任务
	 */
	@Scheduled(cron = "0/30 * * * * *")
	public void userMsgPush() {
		logger.info("检查个人消息推送任务标识:"+runFlag);
		if (runFlag) {
			runFlag = false;
			pushUserMsg();
			runFlag = true;
			// oldPushUserMsg();
		}

	}

	private void pushUserMsg() {
		Map<Long, WebSocketSession> userSessions = webSocketPushHandler.getUsersessions();
		for (Long userId : userSessions.keySet()) { 
			try {
				userQueue.put(userId);
			} catch (Exception e) {
				logger.error("推送个人消息数据异常:userId="+userId,e);
			}
		} 
	}

	private void oldPushUserMsg() {
		ExecutorService fixedThreadPool = null;
		// logger.info("每30秒执行一次定时任务推送消息");
		try {
			logger.info("开始多线程推送一对一消息,以用户为一个维度");
			List<SocketMessage> allTargetMsgList = socketMessageService
					.selectForTargetUserMsgByStatus(MessageStatus.pushed_response + "", WebSocketChatType.single);
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

			logger.info("userMsgListMap=== {}", userMsgListMap);

			fixedThreadPool = Executors.newFixedThreadPool(threadPoolSize);
			List<Future<Integer>> futureList = new ArrayList<Future<Integer>>();
			for (Long key : userMsgListMap.keySet()) {
				final List<SocketMessage> targetMsgList = userMsgListMap.get(key);
				futureList.add(fixedThreadPool.submit(new Callable<Integer>() {
					@Override
					public Integer call() throws Exception {
						try {
							logger.info("给用户[{}]推送消息数:{}", key, targetMsgList.size());
							webSocketPushHandler.sendPushedResponseMessage(targetMsgList);
						} catch (Exception e) {
							logger.error("消息定时推送异常", e);
							return -1;
						}
						return 1;
					}
				}));
			}

			for (Future<Integer> future : futureList) {
				try {
					future.get(10, TimeUnit.SECONDS);
				} catch (Exception e) {
					logger.error("消息结果获取异常", e);
				}
			}

		} catch (Exception e) {
			logger.error("处理一对一单人消息推送异常", e);
		} finally {
			if (fixedThreadPool != null) {
				fixedThreadPool.shutdown();
			}
		}
	}
}
