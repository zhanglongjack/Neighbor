package com.neighbor.app.robot.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.neighbor.app.common.util.RandomUtil;
import com.neighbor.app.group.entity.GroupMember;
import com.neighbor.app.group.service.GroupService;
import com.neighbor.app.packet.entity.Packet;
import com.neighbor.app.packet.service.PacketService;
import com.neighbor.app.robot.entity.GrapPacketData;
import com.neighbor.app.robot.entity.RobotConfig;
import com.neighbor.common.constants.CommonConstants;
import com.neighbor.common.constants.EnvConstants;
import com.neighbor.common.websoket.util.GroupMsgPushHandler;

@Component
public class RobotAutoSendPacket implements ApplicationListener<ContextRefreshedEvent>{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private GroupMsgPushHandler groupMsgPushHandler;
	@Autowired
	private GroupService groupService;
	@Autowired
	private PacketService packetService;
	@Autowired
	private BlockingQueue<GrapPacketData> taskQueue;
    @Autowired
    private CommonConstants commonConstants;
    
	//private static double limitAmount;
//	private static int sleepChance = 300;
	private static int threadPoolSize = 300;
	private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(threadPoolSize);
	private static Map<String,Long> runRobot = new HashMap<>();
	
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		init();
	}
	public void init() {
		//limitAmount = Double.parseDouble(env.getProperty(EnvConstants.ROBOT_SEND_LIMIT_AMOUNT));
		fixedThreadPool.execute(new Runnable() {
			public void run() {
				try {
					List<GroupMember> memberList = groupService.selectRobotGroupMemberBy(null);

					for (final GroupMember member : memberList) {
						addGrapRobot(member);
					}
				} catch (Exception e) {
					logger.error("定时发红包任务,查询群信息异常");
				}
			}

		});
	}
	
	private String getRunRobotKey(GroupMember member) {
		return member.getUserId()+"-"+member.getGroupId();
	}

	public void addGrapRobotBy(Long[] ids) {
		fixedThreadPool.execute(new Runnable() {
			public void run() {
				try {
					logger.info("开始添加机器人发包任务");
					GroupMember member = new GroupMember();
					member.setRobot(new RobotConfig());
					for (Long id : ids) {
						member.getRobot().setRobotId(id);
						List<GroupMember> memberList = groupService.selectRobotGroupMemberBy(member);
						if(memberList.size()>0){
							addGrapRobot(memberList.get(0));
						}
					}
				} catch (Exception e) {
					logger.error("定时发红包任务,查询群信息异常");
				}
			}
		});
	}
	
	public void addGrapRobot(final GroupMember member) {
		String key = getRunRobotKey(member);
		logger.info("机器人运行编号:{}",key);
		if(runRobot.containsKey(key)){
			logger.info("机器人运行编号{}已在运行发包中...",key);
			return;
		}
		
		if (member.getRobot().getSendPacketChance().doubleValue() == 0) {
			logger.info("机器人成员发包配置为0,表示不发包,机器人信息:{}", member.getRobot());
			return;
		}
		
		fixedThreadPool.execute(new Runnable() {
			public void run() {
				robotGrapPacket(member);
			}
		});
		logger.info("发红包线程池任务数量约:" + fixedThreadPool);
	}

	public void robotGrapPacket(GroupMember paramMember) {
		String key = getRunRobotKey(paramMember);
		runRobot.put(key, null);
		logger.info("开始启动机器人发包线程:{}",key);
		GroupMember member = paramMember;
		boolean isRunning = checkMemberSendPacket(member);
		while (isRunning) {
			try {
				sendPacket(member);
				int chance = (int) (100 - member.getRobot().getSendPacketChance()*100);
				chance = RandomUtil.getRandomBy((int)(chance*10));
				int seconds = (chance>10?chance:10) * 1000;//发包最少间隔10秒一次
				try {
					logger.info("机器人{}休眠[{}]毫秒后再发包",member.getUserId(),seconds);
					Thread.sleep(seconds);
				} catch (Exception e) {
				}

				List<GroupMember> checkMemberList = groupService.selectRobotGroupMemberBy(member);
				if(checkMemberList.size()>0){
					member = checkMemberList.get(0);
					isRunning = checkMemberSendPacket(member);
				}else{
					isRunning = false;
				}
				logger.info("机器人{}是否进行下一轮发送红包工作:{}",member.getUserId(),isRunning);
			} catch (Exception e) { 
				logger.error("机器人发红包异常,机器用户编号:"+member.getUserId(),e);
			}
		}
		
		runRobot.remove(key);
		logger.info("停止发红包的机器人:{}",key);

	}

	private void sendPacket(GroupMember member) {
		boolean isSend = member.getRobot().isSend();
		logger.info("机器人成员开始检查是否发红包:{},成员信息:{},机器人信息:{}", isSend, member,member.getRobot());
		if(!isSend){
			logger.info("本次不发送红包:{}",member);
			return;
		}
		
		String packetBaseNum = commonConstants.getDictionarysBy(EnvConstants.PACKET_CONF, EnvConstants.PACEKT_BASE_NUM);
		
		logger.info("开始配置发红包信息");
		String limit[] = member.getGroup().getRedPackAmountLimit().split("-");
		int begin = Integer.parseInt(limit[0]);
		int end = Integer.parseInt(limit[1]);
		int randomNum = RandomUtil.getRandomBy((int)(end - begin)/10)*10 + begin;
		Packet packet = new Packet();
		packet.setAmount(new BigDecimal(randomNum));
		packet.setHitNum(RandomUtil.getRandomBy(10));
		packet.setPacketNum(Integer.parseInt(packetBaseNum));
		packet.setUserId(member.getUserId());
		packet.setGroupId(member.getGroupId());
		packet.setHeadUrl(member.getUser().getUserPhoto());
		packet.setNickName(member.getUser().getNickName());
		packet.setHitChance(member.getRobot().getHitChance());
		try {
			
			Packet resultPacket = packetService.sendPacket(packet, member.getWallet(),member.getUser());
			logger.info("红包编号:{},红包中雷概率:{}",packet.getId(),packet.getHitChance());
			groupMsgPushHandler.pushMessageToGroup(packet);
			addGrapPacketTask(member.getGroupId(), member.getGroup().getGameId(), resultPacket);
		} catch (Exception e) {
			logger.error("机器人发红包出现异常,机器人编号:{},异常信息:{}", member.getRobot().getRobotId(), e);
		}
		
		logger.info("红包信息配置推送完成");
	}

	private boolean checkMemberSendPacket(GroupMember member) {
		String limitAmountStr = commonConstants.getDictionarysBy(EnvConstants.PACKET_CONF, EnvConstants.ROBOT_SEND_LIMIT_AMOUNT);
		BigDecimal limitAmount = new BigDecimal(limitAmountStr);
		return member.getRobot().getStatus()==1 && member.getRobot().getSendPacketChance()>0 &&
				member.getWallet().getAvailableAmount().doubleValue() >= limitAmount.doubleValue();
	}

	/**
	 * 定时发红包任务
	 */
	// @Scheduled(cron = "0 0/1 * * * *")
//	public void grapPacket() {
//		logger.info("当前线程池任务数量约:" + fixedThreadPool);
//		try {
//			List<GroupMember> memberList = groupService.selectRobotGroupMemberBy(null);
//
//			for (final GroupMember member : memberList) {
//				if (member.getRobot().getSendPacketChance().doubleValue() == 0) {
//					logger.info("机器人成员发包配置为0,表示不发包,机器人信息:{}", member.getRobot());
//					continue;
//				}
//				fixedThreadPool.execute(new Runnable() {
//					public void run() {
//						boolean isSend = checkMemberSendPacket(member);
//
//						logger.info("机器人成员开始检查是否发红包:{},成员信息:{}", isSend, member);
//						if (isSend) {
//							logger.info("开始配置发红包信息");
//							String limit[] = member.getGroup().getRedPackAmountLimit().split("-");
//							int begin = Integer.parseInt(limit[0]);
//							int end = Integer.parseInt(limit[1]);
//							int randomNum = RandomUtil.getRandomBy(end - begin + 1) + begin;
//							Packet packet = new Packet();
//							packet.setAmount(new BigDecimal(randomNum));
//							packet.setHitNum(RandomUtil.getRandomBy(10));
//							packet.setPacketNum(7);
//							packet.setUserId(member.getUserId());
//							packet.setGroupId(member.getGroupId());
//							packet.setHeadUrl(member.getUser().getUserPhoto());
//							packet.setNickName(member.getUser().getNickName());
//
//							try {
//								Packet resultPacket = packetService.sendPacket(packet, member.getWallet());
//								addGrapPacketTask(member.getGroupId(), member.getGroup().getGameId(), resultPacket);
//							} catch (Exception e) {
//								logger.error("机器人发红包出现异常,机器人编号:{},异常信息:{}", member.getRobot().getRobotId(), e);
//							}
//
//							groupMsgPushHandler.pushMessageToGroup(packet);
//							logger.info("红包信息配置推送完成");
//						}
//					}
//				});
//			}
//
//		} catch (Exception e) {
//			logger.error("定时发红包任务,查询群信息异常");
//		}
//
//	}

	private void addGrapPacketTask(Long groupId, Long gameId, Packet packet) {
		GrapPacketData data = new GrapPacketData();
		data.setGameId(gameId);
		data.setGroupId(packet.getGroupId());
		data.setPacket(packet);
		try {
			logger.info("开始尝试加人机器人抢红包队列任务,队列当前剩余数量:{}",taskQueue.size());
			taskQueue.put(data);
			logger.info("是否加入机器人抢红包队列任务中?队列当前数量={},data={}",taskQueue.size(),data);
		} catch (Exception e) {
			logger.error("线程启动", e);
		}
	}

	@PreDestroy
	public void destroy() {
		logger.info("定时发红包任务任务线程池准备销毁");
		fixedThreadPool.shutdown();
	}

}
