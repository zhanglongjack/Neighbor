package com.neighbor.app.robot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.neighbor.app.robot.entity.RobotConfig;

@Mapper
public interface RobotConfigMapper {

    int insertSelective(RobotConfig record);

    RobotConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RobotConfig record);

    Long selectPageTotalCount(RobotConfig record);

    List<RobotConfig> selectPageByObjectForList(RobotConfig record);
    
}