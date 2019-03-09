package com.neighbor.app.robot.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neighbor.app.robot.dao.RobotConfigMapper;
import com.neighbor.app.robot.entity.RobotConfig;
import com.neighbor.app.robot.service.RobotConfigService;

@Service
public class RobotConfigServiceImpl implements RobotConfigService {
	
	@Autowired
	private RobotConfigMapper robotConfigMapper;
	
	
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
    
}