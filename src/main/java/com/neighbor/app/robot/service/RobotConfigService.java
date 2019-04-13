package com.neighbor.app.robot.service;

import java.util.List;

import com.neighbor.app.robot.entity.GrapPacketData;
import com.neighbor.app.robot.entity.RobotConfig;

public interface RobotConfigService {

    int insertSelective(RobotConfig record);

    RobotConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RobotConfig record);

    Long selectPageTotalCount(RobotConfig record);

    List<RobotConfig> selectPageByObjectForList(RobotConfig record);

	void robotGrapPacket(GrapPacketData data);

	void batchUpdateRobotStatus(Long[] ids, int status);

}
