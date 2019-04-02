package com.neighbor.app.robot.service;

import java.util.List;

import com.neighbor.app.robot.entity.RobotGroupRelationKey;

public interface RobotGroupRelationService {

    int insertSelective(RobotGroupRelationKey record);

	int deleteRobotRelationByGroups(RobotGroupRelationKey record);

	List<RobotGroupRelationKey> selectRelationListBy(RobotGroupRelationKey record);

	void batchInsert(RobotGroupRelationKey record, Long[] ids);
 
}
