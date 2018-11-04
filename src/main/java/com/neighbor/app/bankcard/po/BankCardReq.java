package com.neighbor.app.bankcard.po;

import com.neighbor.app.api.common.BaseApiReq;

public class BankCardReq extends BaseApiReq {

	private Integer uId;
	private String dealName;
	private String bankCardNo;
	private String branchName;
    private String createTime;
    private String updateTime;
	    
	public Integer getuId() {
		return uId;
	}
	public void setuId(Integer uId) {
		this.uId = uId;
	}
	public String getDealName() {
		return dealName;
	}
	public void setDealName(String dealName) {
		this.dealName = dealName;
	}
	public String getBankCardNo() {
		return bankCardNo;
	}
	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
}
