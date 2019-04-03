package com.neighbor.app.robot.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.neighbor.app.group.entity.GroupMember;
import com.neighbor.app.group.service.GroupService;
import com.neighbor.app.robot.dao.RobotGroupRelationMapper;
import com.neighbor.app.robot.entity.RobotGroupRelationKey;
import com.neighbor.app.robot.service.RobotGroupRelationService;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.users.service.UserService;

@Service
public class RobotGroupRelationServiceImpl implements RobotGroupRelationService {

	@Autowired
	private RobotGroupRelationMapper robotGroupRelationMapper;
	@Autowired
	private GroupService groupService;
	@Autowired
	private UserService userService;
	
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int insertSelective(RobotGroupRelationKey relation) {
		List<RobotGroupRelationKey> resultList = selectRelationListBy(relation);
		if(resultList.size()>0){
			return 0;
		}
		UserInfo user = userService.selectByRobotId(relation.getRobotId());
		groupService.addGroupMember(relation.getGroupId(), user.getId(), null);
		return robotGroupRelationMapper.insertSelective(relation);
	}

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int deleteRobotRelationByGroups(RobotGroupRelationKey record) throws Exception {
    	
    	if(record.getGroupId()!=null && record.getRobotId()!=null){
    		exitGroup(record.getRobotId(), record.getGroupId());
    	}else if(record.getGroupId()==null){
    		String groupIds[] = record.getGroupIdInStr().split(",");
    		for(String groupId : groupIds){
    			exitGroup(record.getRobotId(), Long.parseLong(groupId));
    		}
    		
    	}else if(record.getRobotId()==null){
    		String robotIds[] = record.getRobotIdInStr().split(",");
    		
    		for(String robotId : robotIds){
    			exitGroup(Long.parseLong(robotId), record.getGroupId());
    		}
    	}
    	
		return robotGroupRelationMapper.deleteRelationBy(record);
	}

	private void exitGroup(Long robotId, Long groupId) throws Exception {
		UserInfo user = userService.selectByRobotId(robotId);
		GroupMember member = groupService.selectGroupMemberBy(user.getId(), groupId);
		
		groupService.exitGroup(user, member);
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
			insertSelective(relation);
		}
		
	}
	

	
	

	
	
	
	
	
	
	
	
	
	
	
 
}
