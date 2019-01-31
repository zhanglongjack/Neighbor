package com.neighbor.app.withdraw.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.neighbor.app.balance.po.TransactionItemDesc;
import com.neighbor.app.balance.po.TransactionTypeDesc;
import com.neighbor.app.common.entity.PageEntity;
import com.neighbor.app.withdraw.constants.WithdrawStatusDesc;
import com.neighbor.common.util.DateFormateType;
import com.neighbor.common.util.DateUtils;

public class Withdraw extends PageEntity {
    private Long id;
    private Long recordId;

    private String orderNo;

    private Date createTime;

    private Date updateTime;

    private Long uId;

    private BigDecimal amount;

    private String bankCardNo;

    private String branchInfo;

    private String realName;

    private String states;
    private String statesStr;

    private String remarks;

    private BigDecimal availableAmount;
    private String createTimeStr;

    private BigDecimal actualAmount; //实际到账金额

    private BigDecimal cost; //提现费用

    private String isCustomer;//客户审核提现记录

    private String transactionTypeDesc;

    private String phone;
    private String verfiyCode;
    private String shortDate;

    public String getShortDate() {
        return shortDate;
    }

    public void setShortDate(String shortDate) {
        this.shortDate = shortDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerfiyCode() {
        return verfiyCode;
    }

    public void setVerfiyCode(String verfiyCode) {
        this.verfiyCode = verfiyCode;
    }

    public String getIsCustomer() {
        return isCustomer;
    }

    public void setIsCustomer(String isCustomer) {
        this.isCustomer = isCustomer;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getTransactionTypeDesc() {
        return TransactionTypeDesc.payment.getDes();
    }

    public void setTransactionTypeDesc(String transactionTypeDesc) {
        this.transactionTypeDesc = transactionTypeDesc;
    }

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
        if(remarks==null){
            return TransactionItemDesc.withdraw.getDes();
        }
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }

    public String getCreateTimeStr() {
        if(createTime!=null){
            try {
                return DateUtils.formatDateStr(createTime, DateFormateType.LANG_FORMAT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getStatesStr() {
        if(states!=null){
            return WithdrawStatusDesc.getDesByValue(states);
        }
        return statesStr;
    }

    public void setStatesStr(String statesStr) {
        this.statesStr = statesStr;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Withdraw{");
        sb.append("id=").append(id);
        sb.append(", orderNo='").append(orderNo).append('\'');
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", uId=").append(uId);
        sb.append(", amount=").append(amount);
        sb.append(", bankCardNo='").append(bankCardNo).append('\'');
        sb.append(", branchInfo='").append(branchInfo).append('\'');
        sb.append(", realName='").append(realName).append('\'');
        sb.append(", states='").append(states).append('\'');
        sb.append(", remarks='").append(remarks).append('\'');
        sb.append(", availableAmount=").append(availableAmount);
        sb.append(", createTimeStr='").append(createTimeStr).append('\'');
        sb.append(", actualAmount=").append(actualAmount);
        sb.append(", cost=").append(cost);
        sb.append('}');
        return sb.toString();
    }
}