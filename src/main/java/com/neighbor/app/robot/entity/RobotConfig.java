package com.neighbor.app.robot.entity;

import com.neighbor.app.common.entity.PageEntity;
import com.neighbor.app.robot.util.RandomUtil;
import com.neighbor.app.users.entity.UserInfo;
import com.neighbor.app.wallet.entity.UserWallet;

public class RobotConfig extends PageEntity {
	private Integer robotId;

	private Double hitChance;

	private Double grapChance;

	private Double sendPacketChance;

	private Integer status;

	private Long updateDateTime;

	private UserInfo user;
	private UserWallet wallet;

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	public UserWallet getWallet() {
		return wallet;
	}

	public void setWallet(UserWallet wallet) {
		this.wallet = wallet;
	}

	public boolean isHit() {
		return RandomUtil.getRandomBy(100) <= hitChance * 100;
	}

	public boolean isSend() {
		return RandomUtil.getRandomBy(100) <= sendPacketChance * 100;
	}

	public boolean isGrap() {
		return RandomUtil.getRandomBy(100) <= grapChance * 100;
	}

	public Integer getRobotId() {
		return robotId;
	}

	public void setRobotId(Integer robotId) {
		this.robotId = robotId;
	}

	public Double getHitChance() {
		return hitChance;
	}

	public void setHitChance(Double hitChance) {
		this.hitChance = hitChance;
	}

	public Double getGrapChance() {
		return grapChance;
	}

	public void setGrapChance(Double grapChance) {
		this.grapChance = grapChance;
	}

	public Double getSendPacketChance() {
		return sendPacketChance;
	}

	public void setSendPacketChance(Double sendPacketChance) {
		this.sendPacketChance = sendPacketChance;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getUpdateDateTime() {
		return updateDateTime;
	}

	public void setUpdateDateTime(Long updateDateTime) {
		this.updateDateTime = updateDateTime;
	}
}