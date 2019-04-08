package com.neighbor.app.robot.service;

import com.neighbor.app.robot.entity.RobotGroupRelationKey;

public interface RobotGroupRelationService {

	void insertSelective(RobotGroupRelationKey record);

    void deleteRobotRelationByGroups(RobotGroupRelationKey record) throws Exception;

	void batchInsert(RobotGroupRelationKey record, Long[] ids);
 
}
