package com.neighbor.schedule.trigger;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.neighbor.app.notice.entity.SysNotice;
import com.neighbor.app.notice.service.SysNoticeService;
import com.neighbor.common.util.ResponseResult;
import com.neighbor.common.websoket.po.SocketMessage;
import com.neighbor.common.websoket.service.SocketMessageService;
import com.neighbor.common.websoket.util.WebSocketPushHandler;

@Component
public class NoticeHandleSchedule {
	private static final Logger logger = LoggerFactory.getLogger(NoticeHandleSchedule.class);
	@Autowired
	private WebSocketPushHandler webSocketPushHandler;
	@Autowired
	private SocketMessageService socketMessageService;
	@Autowired
	private SysNoticeService sysNoticeService;
	
	private final  static int threadPoolSize = Runtime.getRuntime().availableProcessors() + 1;
	private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(threadPoolSize);
	private static BlockingQueue<SysNotice> userQueue = new LinkedBlockingQueue<SysNotice>(threadPoolSize);
	private static boolean runFlag = true;
	private static boolean isRunning = true;

	@PostConstruct
	public void init() {
		fixedThreadPool.execute(new Runnable() {
			public void run() {
				logger.info("开始公告消息推送任务队列消费！");
				try {
					while (isRunning) {
						try {
							logger.info("开始取公告推送消息的队列数据");
							SysNotice notice = userQueue.take();
							logger.info("检查是否获得公告推送消息的队列数据:{}", notice);

							fixedThreadPool.execute(new Runnable() {
								@Override
								public void run() {
									try {
										SocketMessage msgInfo = socketMessageService.forceOfflineNoticeBuild();
										msgInfo.setContent(JSON.toJSONString(notice));
										ResponseResult handleResult = new ResponseResult(); // 消息已接收
										handleResult.setRequestID(msgInfo.getWebSocketHeader().getRequestId());
										handleResult.addBody("msgType", msgInfo.getMsgType());
										handleResult.addBody("chatType", msgInfo.getChatType());
										handleResult.addBody("msgInfo", msgInfo);
										logger.info("强制下线消息推送开始,公告:{}", notice);
										webSocketPushHandler.forceAllUserOffline(handleResult);
									} catch (Exception e) {
										logger.error("个人消息定时推送异常,公告:" + notice, e);
									}
								}
							});

						} catch (Exception e) {
							logger.error("公告消息推送任务添加失败", e);
						}

					}
				} catch (Exception e) {
					logger.error("公告队列消费异常", e);
					Thread.currentThread().interrupt();
				} finally {
					isRunning = false;
				}
			}
		});
	}

	@PreDestroy
	public void destroy() {
		logger.info("公告消息定时推送任务线程池准备销毁");
		if (fixedThreadPool != null) {
			fixedThreadPool.shutdown();
		}
	}

	/**
	 * 公告消息定时推送任务
	 */
	@Scheduled(cron = "0 0/1 * * * *")
	public void noticeHandle() {
		logger.info("公告消息推送任务标识:" + runFlag);
		if (runFlag) {
			runFlag = false;
			try {
				handleNoticStatus();
				forceOffline();
			} catch (Exception e) {
				logger.error("",e);
			}
			runFlag = true;
		}

	}

	private void handleNoticStatus() {
		List<SysNotice> noticeList = null;
		SysNotice record = new SysNotice();
		record.setStatus(0);

		noticeList = sysNoticeService.selectBySelective(record);
		updateNotice(noticeList,1,true);
		
		record.setStatus(1);
		noticeList = sysNoticeService.selectBySelective(record);
		updateNotice(noticeList,2,false);
	}

	private void updateNotice(List<SysNotice> noticeList,int status,boolean isBegin) {
		if(noticeList.size()>0){
			SysNotice updateNotice = new SysNotice();
			for (SysNotice notice : noticeList) {
				updateNotice.setNoticeId(notice.getNoticeId());
				updateNotice.setStatus(status);
				try {
					if(isBegin&&notice.isShowNotice()
							||!isBegin&&notice.isCloseNotice()){
						sysNoticeService.updateByPrimaryKeySelective(updateNotice);
					}
				} catch (Exception e) {
					logger.error("更新公告状态异常,公告编号:" + notice.getNoticeId(), e);
				}
			}
		}
	}

	private void forceOffline() {
		SysNotice record = new SysNotice();
		record.setStatus(1);
		List<SysNotice> noticeList = sysNoticeService.selectBySelective(record);
		if(noticeList.size()>0){
			for (SysNotice notice : noticeList) {
				if(!notice.isForceOffline()){
					try {
						userQueue.put(notice);
					} catch (Exception e) {
						logger.error("添加公告消息队列异常:" + notice, e);
					}
					break;
				}
			}
		}
	}

}
