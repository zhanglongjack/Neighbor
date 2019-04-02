package com.neighbor.app.robot.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.neighbor.app.group.service.GroupService;
import com.neighbor.app.robot.dao.RobotGroupRelationMapper;
import com.neighbor.app.robot.entity.RobotGroupRelationKey;
import com.neighbor.app.robot.service.RobotGroupRelationService;

@Service
public class RobotGroupRelationServiceImpl implements RobotGroupRelationService {

	@Autowired
	private RobotGroupRelationMapper robotGroupRelationMapper;

	
	@Override
	public int insertSelective(RobotGroupRelationKey record) {
		return robotGroupRelationMapper.insertSelective(record);
	}

	@Override
	public int deleteRobotRelationByGroups(RobotGroupRelationKey record) {
		return robotGroupRelationMapper.deleteRelationBy(record);
	}
 
	@Override
	public List<RobotGroupRelationKey> selectRelationListBy(RobotGroupRelationKey record) {
		return robotGroupRelationMapper.selectRelationListBy(record);
	}

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void batchInsert(RobotGroupRelationKey record,Long ids[]){
    	
		for(Long key : ids){
			RobotGroupRelationKey relation = new RobotGroupRelationKey();
			relation.setGroupId(record.getGroupId()==null?key:record.getGroupId());
			relation.setRobotId(record.getRobotId()==null?key:record.getRobotId());
			
			List<RobotGroupRelationKey> resultList = selectRelationListBy(relation);
			if(resultList.size()==0){
				insertSelective(relation);
			}
		}
		
		
		
		
		
	}
	

	
	

	
	
	
	
	
	
	
	
	
	
	
 
}
