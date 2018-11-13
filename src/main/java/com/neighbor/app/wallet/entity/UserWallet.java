package com.neighbor.app.wallet.entity;

import java.math.BigDecimal;
import java.util.Date;

public class UserWallet {
    private Long id;

	private Date createTime;

	private Date updateTime;

	private Long uId;

	private BigDecimal score =  new BigDecimal("0.00");

	private BigDecimal availableAmount = new BigDecimal("0.00");

	private BigDecimal freezeAmount = new BigDecimal("0.00");

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getuId() {
		return uId;
	}

	public void setuId(Long uId) {
		this.uId = uId;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public BigDecimal getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
	}

	public BigDecimal getFreezeAmount() {
		return freezeAmount;
	}

	public void setFreezeAmount(BigDecimal freezeAmount) {
		this.freezeAmount = freezeAmount;
	}

	@Override
	public String toString() {
		return String.format(
				"UserWallet [id=%s, createTime=%s, updateTime=%s, uId=%s, score=%s, availableAmount=%s, freezeAmount=%s]",
				id, createTime, updateTime, uId, score, availableAmount, freezeAmount);
	}
	
	

}