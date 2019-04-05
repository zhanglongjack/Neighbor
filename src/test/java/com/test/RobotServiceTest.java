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
import com.neighbor.app.game.entity.Game;
import com.neighbor.app.group.entity.Group;
import com.neighbor.app.robot.entity.RobotConfig;
import com.neighbor.app.robot.service.RobotConfigService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartNeighbor.class)
public class RobotServiceTest { 
	private final Logger logger = LoggerFactory.getLogger(getClass());
 
	@Autowired
	private RobotConfigService robotConfigService;
	@Test
	public void test1() throws Exception{ 
		RobotConfig record = new RobotConfig();
		
		List<RobotConfig> robotList = robotConfigService.selectPageByObjectForList(record);
		System.err.println(robotList.get(0).getUser());
		
	}
	
	@Test
	public void insert() throws Exception{ 
		RobotConfig record = new RobotConfig();
		record.setUpdateDateTime(System.currentTimeMillis());
		
		robotConfigService.insertSelective(record);
		
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
