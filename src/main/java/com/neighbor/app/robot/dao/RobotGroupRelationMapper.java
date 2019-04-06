package com.neighbor.app.robot.dao;

import org.apache.ibatis.annotations.Mapper;

import com.neighbor.app.robot.entity.RobotGroupRelationKey;

@Mapper
public interface RobotGroupRelationMapper {
 
	Long selectRelationCountBy(RobotGroupRelationKey record);

    
    
    
    
    
}