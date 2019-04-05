package com.neighbor.app.robot.entity;

public class RobotGroupRelationKey {
    private Long robotId;

    private Long groupId;
    
    private String robotIdNotInStr;
    
    private String groupIdNotInStr;
    
    private String robotIdInStr;
    
    private String groupIdInStr;

	public Long getRobotId() {
		return robotId;
	}

	public void setRobotId(Long robotId) {
		this.robotId = robotId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getRobotIdNotInStr() {
		return robotIdNotInStr;
	}

	public void setRobotIdNotInStr(String robotIdNotInStr) {
		this.robotIdNotInStr = robotIdNotInStr;
	}

	public String getGroupIdNotInStr() {
		return groupIdNotInStr;
	}

	public void setGroupIdNotInStr(String groupIdNotInStr) {
		this.groupIdNotInStr = groupIdNotInStr;
	}

	public String getRobotIdInStr() {
		return robotIdInStr;
	}

	public void setRobotIdInStr(String robotIdInStr) {
		this.robotIdInStr = robotIdInStr;
	}

	public String getGroupIdInStr() {
		return groupIdInStr;
	}

	public void setGroupIdInStr(String groupIdInStr) {
		this.groupIdInStr = groupIdInStr;
	}

	@Override
	public String toString() {
		return String.format(
				"RobotGroupRelationKey [robotId=%s, groupId=%s, robotIdNotInStr=%s, groupIdNotInStr=%s, robotIdInStr=%s, groupIdInStr=%s]",
				robotId, groupId, robotIdNotInStr, groupIdNotInStr, robotIdInStr, groupIdInStr);
	}


	
    
}