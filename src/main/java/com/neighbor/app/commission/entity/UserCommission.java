package com.neighbor.app.commission.entity;

import java.math.BigDecimal;

import com.neighbor.app.common.entity.PageEntity;
import com.neighbor.app.users.entity.UserInfo;

public class UserCommission extends PageEntity {
	private Long id;

	private Long ownUser;

	private Long downUserId;

	private BigDecimal commisionAmt;

	private String downLevel;

	private String gainProportion;

	private String gainDate;

	private String gainTime;

	private String remarks;

	private UserInfo downUser;

	private BigDecimal totalAmt;
	private BigDecimal todayAmt;
	
	// query
	private String greaterGainDate;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOwnUser() {
		return ownUser;
	}

	public void setOwnUser(Long ownUser) {
		this.ownUser = ownUser;
	}

	public Long getDownUserId() {
		return downUserId;
	}

	public void setDownUserId(Long downUserId) {
		this.downUserId = downUserId;
	}

	public BigDecimal getCommisionAmt() {
		return commisionAmt;
	}

	public void setCommisionAmt(BigDecimal commisionAmt) {
		this.commisionAmt = commisionAmt;
	}

	public String getDownLevel() {
		return downLevel;
	}

	public void setDownLevel(String downLevel) {
		this.downLevel = downLevel;
	}

	public String getGainProportion() {
		return gainProportion;
	}

	public void setGainProportion(String gainProportion) {
		this.gainProportion = gainProportion;
	}

	public String getGainDate() {
		return gainDate;
	}

	public void setGainDate(String gainDate) {
		this.gainDate = gainDate;
	}

	public String getGainTime() {
		return gainTime;
	}

	public void setGainTime(String gainTime) {
		this.gainTime = gainTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public UserInfo getDownUser() {
		return downUser;
	}

	public void setDownUser(UserInfo downUser) {
		this.downUser = downUser;
	}

	public String getGreaterGainDate() {
		return greaterGainDate;
	}

	public void setGreaterGainDate(String greaterGainDate) {
		this.greaterGainDate = greaterGainDate;
	}

	public BigDecimal getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(BigDecimal totalAmt) {
		this.totalAmt = totalAmt;
	}

	public BigDecimal getTodayAmt() {
		return todayAmt;
	}

	public void setTodayAmt(BigDecimal todayAmt) {
		this.todayAmt = todayAmt;
	}

	@Override
	public String toString() {
		return String.format(
				"UserCommission [id=%s, ownUser=%s, downUserId=%s, commisionAmt=%s, downLevel=%s, gainProportion=%s, gainDate=%s, gainTime=%s, remarks=%s, downUser=%s]",
				id, ownUser, downUserId, commisionAmt, downLevel, gainProportion, gainDate, gainTime, remarks,
				downUser);
	}


}