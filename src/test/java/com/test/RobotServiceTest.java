package com.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neighbor.StartNeighbor;
import com.neighbor.app.group.entity.Group;
import com.neighbor.app.group.entity.GroupMember;
import com.neighbor.app.group.service.GroupService;
import com.neighbor.app.robot.entity.RobotConfig;
import com.neighbor.app.robot.service.RobotConfigService;
import com.neighbor.app.robot.service.RobotGroupRelationService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartNeighbor.class)
public class RobotServiceTest { 
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private GroupService groupService; 
	@Autowired
	private RobotConfigService robotConfigService;
	@Autowired
	private RobotGroupRelationService robotGroupRelationService;
	@Test
	public void test1() throws Exception{ 
		GroupMember memberParam = new GroupMember();
		memberParam.setGroupId(100005l);
		List<GroupMember> memberList = groupService.selectRobotGroupMemberBy(memberParam);
		
		for(GroupMember member : memberList){
			logger.info("机器人编号:{},机器人用户编号:{},群成员编号:{},群编号:{},钱包:{}",
					member.getRobot().getRobotId(),
					member.getUser().getId(),
					member.getId(),
					member.getGroup().getId(),
					member.getWallet().getId());
		}
		
		
		
//		RobotGroupRelationKey record = new RobotGroupRelationKey();
//		record.setGroupId(100005l);
//		record.setRobotId(100010l);
//		robotGroupRelationService.deleteRobotRelationByGroups(record);
//		RobotConfig record = new RobotConfig();
//		List<RobotConfig> robotList = robotConfigService.selectPageByObjectForList(record);
//		System.err.println(robotList.get(0).getUser());
		
	}
	
	@Test
	public void insert() throws Exception{ 
		RobotConfig record = new RobotConfig();
		record.setUpdateDateTime(System.currentTimeMillis());
		
//		robotConfigService.insertSelective(record);
		
	}
	 
	public static void main(String[] args) {
		 ObjectMapper mapper = new ObjectMapper();
		 Group group = new Group();
		 group.setRobot(new RobotConfig());
//		 group.setGame(new Game());
		 try {
			System.out.println(mapper.writeValueAsString(group));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
}
