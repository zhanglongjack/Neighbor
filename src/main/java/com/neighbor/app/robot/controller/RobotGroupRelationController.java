package com.neighbor.app.robot.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.neighbor.app.robot.entity.RobotGroupRelationKey;
import com.neighbor.app.robot.service.RobotGroupRelationService;
import com.neighbor.common.util.ResponseResult;

@Controller
@RequestMapping(value = "/gobotGroupRelation")
@SessionAttributes("user")
public class RobotGroupRelationController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private RobotGroupRelationService robotGroupRelationService;


	@RequestMapping(value="/robotGroupDelete.ser")
	@ResponseBody
	public ResponseResult robotGroupDelete(RobotGroupRelationKey record){
		logger.info("robotGroupDelete request:{}",record);
		robotGroupRelationService.deleteRobotRelationByGroups(record);
		return new ResponseResult();
	}
	
	@RequestMapping(value="/robotGroupAdd.ser")
	@ResponseBody
	public ResponseResult robotGroupAdd(RobotGroupRelationKey record) throws Exception{
		logger.info("robotGroupAdd request:{}",record);
		robotGroupRelationService.insertSelective(record);
		return new ResponseResult();
	}
	@RequestMapping(value="/batchRobotGroupAdd.ser")
	@ResponseBody
	public ResponseResult batchRobotGroupAdd(RobotGroupRelationKey record,@RequestParam(value="ids[]")Long ids[]) throws Exception{
		logger.info("batchRobotGroupAdd request:机器人编号{},群编号{}",record,ids);
		robotGroupRelationService.batchInsert(record,ids);
		
		
		return new ResponseResult();
	}
}
