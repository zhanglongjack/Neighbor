package com.neighbor.app.bankcard.entity;

import java.util.Date;

import com.neighbor.app.common.entity.PageEntity;

public class BankCard extends PageEntity {
    private Long id;

    private Date createTime;

    private Date updateTime;

    private Long uId;

    private String bankCardNo;

    private String branchInfo;

    private String realName;

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

	@Override
	public String toString() {
		return String.format(
				"BankCard [id=%s, createTime=%s, updateTime=%s, uId=%s, bankCardNo=%s, branchInfo=%s, realName=%s]", id,
				createTime, updateTime, uId, bankCardNo, branchInfo, realName);
	}
    
    
    
}