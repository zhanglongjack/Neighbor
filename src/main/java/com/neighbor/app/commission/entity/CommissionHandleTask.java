package com.neighbor.app.commission.entity;

import java.math.BigDecimal;

public class CommissionHandleTask {
	private Long commissionId;

	private Long groupMasterUId;

	private Long userId;
	
	private Long gameId;

	private BigDecimal splitAmount;

	private Integer status; // 状态:0-未处理,1-处理中,2-处理完成
	private Integer oldStatus; // 状态:0-未处理,1-处理中,2-处理完成

	private String createdTime;

	private String updatedTime;

	public Long getCommissionId() {
		return commissionId;
	}

	public void setCommissionId(Long commissionId) {
		this.commissionId = commissionId;
	}

	public Long getGroupMasterUId() {
		return groupMasterUId;
	}

	public void setGroupMasterUId(Long groupMasterUId) {
		this.groupMasterUId = groupMasterUId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public BigDecimal getSplitAmount() {
		return splitAmount;
	}

	public void setSplitAmount(BigDecimal splitAmount) {
		this.splitAmount = splitAmount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(Integer oldStatus) {
		this.oldStatus = oldStatus;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

	@Override
	public String toString() {
		return String.format(
				"CommissionHandleTask [commissionId=%s, groupMasterUId=%s, userId=%s, splitAmount=%s, status=%s, createdTime=%s, updatedTime=%s]",
				commissionId, groupMasterUId, userId, splitAmount, status, createdTime, updatedTime);
	}

}