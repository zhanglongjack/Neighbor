package com.test;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.neighbor.StartNeighbor;
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
	 
}
