package com.neighbor.app.robot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.neighbor.app.robot.entity.RobotGroupRelationKey;

@Mapper
public interface RobotGroupRelationMapper {

    int insertSelective(RobotGroupRelationKey record);

	int deleteRelationBy(RobotGroupRelationKey record);
	
	List<RobotGroupRelationKey> selectRelationListBy(RobotGroupRelationKey record);

    
    
    
    
    
}