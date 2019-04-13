package com.neighbor.app.robot.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neighbor.app.common.util.RandomUtil;
import com.neighbor.app.group.entity.GroupMember;
import com.neighbor.app.group.service.GroupService;
import com.neighbor.app.packet.service.PacketService;
import com.neighbor.app.robot.dao.RobotConfigMapper;
import com.neighbor.app.robot.entity.GrapPacketData;
import com.neighbor.app.robot.entity.RobotConfig;
import com.neighbor.app.robot.service.RobotConfigService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.users.service.UserService;
import com.neighbor.common.util.ResponseResult;

@Service
public class RobotConfigServiceImpl implements RobotConfigService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private RobotConfigMapper robotConfigMapper;
	@Autowired
	private GroupService groupService;
	@Autowired
	private PacketService packetService;
	@Autowired
	private UserService userService;
	
	@Override
	public int insertSelective(RobotConfig record) {
		return robotConfigMapper.insertSelective(record);
	}

	@Override
	public RobotConfig selectByPrimaryKey(Integer id) {
		return robotConfigMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(RobotConfig record) {
		return robotConfigMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public Long selectPageTotalCount(RobotConfig record) {
		return robotConfigMapper.selectPageTotalCount(record);
	}

	@Override
	public List<RobotConfig> selectPageByObjectForList(RobotConfig record) {
		return robotConfigMapper.selectPageByObjectForList(record);
	}

	@Override
	public void robotGrapPacket(GrapPacketData data){
		logger.info("队列开始抢红包:{}",data);
		ExecutorService fixedThreadPool = null;
		GroupMember memberParam = new GroupMember();
		memberParam.setGroupId(data.getGroupId());
		List<GroupMember> memberList = groupService.selectRobotGroupMemberBy(memberParam);
		try {
			if(memberList.size()==0){
				logger.info("无机器人配置");
				return;
			}
			logger.info("有多少群成员:"+memberList.size());
			fixedThreadPool = Executors.newFixedThreadPool(memberList.size() > 32 ? 32 : memberList.size());
			List<Future<Long>> futureList = new ArrayList<Future<Long>>();
			for (int i = 0; i < memberList.size(); i++) {
				final GroupMember member = memberList.get(i);
				futureList.add(fixedThreadPool.submit(new Callable<Long>() {
					@Override
					public Long call() throws Exception {
						UserInfo user = null;
						try {
							user = userService.selectByPrimaryKey(member.getUserId());
							int second = RandomUtil.getRandomBy(100 * 200)+500;
							logger.info("机器编号{}睡眠:{}",user.getRobotSno(),(second));
							Thread.sleep(second);// 睡second毫秒后抢
							
							ResponseResult result = packetService.grabPacekt(data.getPacket(), user, data.getGameId());
							logger.info("机器编号{}抢完红包的信息:"+result,user.getRobotSno());
							if (result.getErrorCode() == 0) {
								return member.getUserId();
							}
							return null;
						} catch (Exception e) {
							e.printStackTrace();
							logger.error("异常的用户:" + user);
							return null;
						}
					}
				}));
			}

			for (Future<Long> future : futureList) {
				try {
					future.get(100, TimeUnit.SECONDS);
				} catch (Exception e) { }
			}
			logger.info("机器抢红包结束");
		} catch (Exception e) {
			logger.error("机器人抢红包异常:"+e.getMessage(),e);
		} finally {
			if (fixedThreadPool != null) {
				fixedThreadPool.shutdown();
			}
		}
	
	}

	@Override
	public void batchUpdateRobotStatus(Long[] ids,int status) {
		RobotConfig robot = new RobotConfig();
		robot.setStatus(status);
		for(Long id : ids){
			robot.setRobotId(id);
			robotConfigMapper.updateByPrimaryKeySelective(robot);
		}
	}

}