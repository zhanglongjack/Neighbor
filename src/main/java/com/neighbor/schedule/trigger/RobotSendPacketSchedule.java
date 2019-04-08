package com.neighbor.schedule.trigger;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.neighbor.app.group.entity.GroupMember;
import com.neighbor.app.group.service.GroupService;
import com.neighbor.app.packet.entity.Packet;
import com.neighbor.app.packet.service.PacketService;
import com.neighbor.app.robot.util.RandomUtil;
import com.neighbor.common.websoket.util.GroupMsgPushHandler;
import com.neighbor.schedule.util.GrapPacketData;

@Component
public class RobotSendPacketSchedule {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private GroupMsgPushHandler groupMsgPushHandler;
	@Autowired
	private GroupService groupService;
	@Autowired
	private PacketService packetService;
	@Autowired
	private BlockingQueue<GrapPacketData> taskQueue;

	private static int limitAmount = 1000;
	private static int threadPoolSize = 16;
	private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(threadPoolSize);

	/**
	 * 定时发红包任务
	 */
	@Scheduled(cron = "0 0/1 * * * *")
	public void run() {
		logger.info("当前线程池任务数量约:" + fixedThreadPool);
		try {
			List<GroupMember> memberList = groupService.selectRobotGroupMemberBy(null);

			for (final GroupMember member : memberList) {
				if(member.getRobot().getSendPacketChance().doubleValue()==0){
					logger.info("机器人成员发包配置为0,表示不发包,机器人信息:{}", member.getRobot());
					continue;
				}
				fixedThreadPool.execute(new Runnable() {
					public void run() {
						boolean isSend = member.getRobot().isSend()
								&& member.getWallet().getAvailableAmount().doubleValue() >= limitAmount;
								
						logger.info("机器人成员开始检查是否发红包:{},成员信息:{}", isSend, member);
						if (isSend) {
							logger.info("开始配置发红包信息");
							String limit[] = member.getGroup().getRedPackAmountLimit().split("-");
							int begin = Integer.parseInt(limit[0]);
							int end = Integer.parseInt(limit[1]);
							int randomNum = RandomUtil.getRandomBy(end - begin + 1) + begin;
							Packet packet = new Packet();
							packet.setAmount(new BigDecimal(randomNum));
							packet.setHitNum(RandomUtil.getRandomBy(10));
							packet.setPacketNum(7);
							packet.setUserId(member.getUserId());
							packet.setGroupId(member.getGroupId());
							packet.setHeadUrl(member.getUser().getUserPhoto());
							packet.setNickName(member.getUser().getNickName());

							try {
								Packet resultPacket = packetService.sendPacket(packet, member.getWallet());
								addGrapPacketTask(member.getGroupId(), member.getGroup().getGameId(), resultPacket);
							} catch (Exception e) {
								logger.error("机器人发红包出现异常,机器人编号:{},异常信息:{}", member.getRobot().getRobotId(), e);
							}

							groupMsgPushHandler.pushMessageToGroup(packet);
							logger.info("红包信息配置推送完成");
						}
					}
				});
			}

		} catch (Exception e) {
			logger.error("定时发红包任务,查询群信息异常");
		}

	}

	private void addGrapPacketTask(Long groupId, Long gameId, Packet packet) {
		GrapPacketData data = new GrapPacketData();
		data.setGameId(gameId);
		data.setGroupId(packet.getGroupId());
		data.setPacket(packet);
		try {
			logger.info("开始尝试加人机器人抢红包队列任务");
			boolean isOk = taskQueue.offer(data);
			logger.info("是否加入机器人抢红包队列任务中?{}", isOk);
		} catch (Exception e) {
			logger.error("线程启动", e);
		}
	}

	@PreDestroy
	public void destroy() {
		System.out.println("线程迟准备销毁");
		fixedThreadPool.shutdown();
	}
}
