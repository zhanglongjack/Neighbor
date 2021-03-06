package com.neighbor.app.wallet.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserWallet {
    private Long id;
    private Long walletId;

	private Date createTime;

	private Date updateTime;

	private Long uId;

	private BigDecimal score =  new BigDecimal("0.00");

	private BigDecimal availableAmount = new BigDecimal("0.00");

	private BigDecimal freezeAmount = new BigDecimal("0.00");

	@JsonIgnore
	private String payPassword;//支付密码
	
	private Boolean isSetPassword = false;

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
		setIsSetPassword(payPassword!=null);
	}

	public Boolean getIsSetPassword() {
		return isSetPassword;
	}

	public void setIsSetPassword(Boolean isSetPassword) {
		this.isSetPassword = isSetPassword;
	}

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

	public Long getWalletId() {
		return walletId;
	}

	public void setWalletId(Long walletId) {
		this.walletId = walletId;
		this.id=walletId;
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
				"UserWallet [id=%s, createTime=%s, updateTime=%s, uId=%s, score=%s, availableAmount=%s, freezeAmount=%s, payPassword=%s]",
				id, createTime, updateTime, uId, score, availableAmount, freezeAmount,payPassword);
	}
	
	

}