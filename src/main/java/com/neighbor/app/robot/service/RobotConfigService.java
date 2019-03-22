package com.neighbor.app.robot.service;

import java.util.List;

import com.neighbor.app.packet.entity.Packet;
import com.neighbor.app.robot.entity.RobotConfig;

public interface RobotConfigService {

    int insertSelective(RobotConfig record);

    RobotConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RobotConfig record);

    Long selectPageTotalCount(RobotConfig record);

    List<RobotConfig> selectPageByObjectForList(RobotConfig record);

	void robotGrapPacket(Long groupId, Long gameId, Packet packet);
}
