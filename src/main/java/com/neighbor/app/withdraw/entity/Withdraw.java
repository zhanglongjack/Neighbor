package com.neighbor.app.withdraw.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.neighbor.app.common.entity.PageEntity;

public class Withdraw extends PageEntity {
    private Long id;

    private String orderNo;

    private Date createTime;

    private Date updateTime;

    private Long uId;

    private BigDecimal amount;

    private String bankCardNo;

    private String branchInfo;

    private String realName;

    private String states;

    private String remarks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getBranchInfo() {
        return branchInfo;
    }

    public void setBranchInfo(String branchInfo) {
        this.branchInfo = branchInfo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

	@Override
	public String toString() {
		return String.format(
				"Withdraw [id=%s, orderNo=%s, createTime=%s, updateTime=%s, uId=%s, amount=%s, bankCardNo=%s, branchInfo=%s, realName=%s, states=%s, remarks=%s]",
				id, orderNo, createTime, updateTime, uId, amount, bankCardNo, branchInfo, realName, states, remarks);
	}


    
}