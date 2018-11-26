package com.neighbor.app.balance.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.neighbor.app.balance.po.TransactionTypeDesc;
import com.neighbor.app.common.entity.PageEntity;
import com.neighbor.common.util.DateFormateType;
import com.neighbor.common.util.DateUtils;

public class BalanceDetail extends PageEntity {
    private Long id;

    private Date createTime;

    private Date updateTime;

    private Long uId;

    private BigDecimal amount;

    private String transactionType;

    private String transactionSubType;

    private BigDecimal availableAmount;

    private Long transactionId;

    private String remarks;
    private String createTimeStr ;

    
    public String getCreateTimeStr() {
        if(createTime!=null){
            try {
                return DateUtils.formatDateStr(createTime,DateFormateType.LANG_FORMAT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return createTimeStr;
    }
    
    public String getTransactionTypeDesc() {
    	return TransactionTypeDesc.valueOf(transactionType).getDes(); 
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTransactionSubType() {
        return transactionSubType;
    }

    public void setTransactionSubType(String transactionSubType) {
        this.transactionSubType = transactionSubType;
    }

	@Override
	public String toString() {
		return String.format(
				"BalanceDetail [id=%s, createTime=%s, updateTime=%s, uId=%s, amount=%s, transactionType=%s, transactionSubType=%s, availableAmount=%s, transactionId=%s, remarks=%s, createTimeStr=%s]",
				id, createTime, updateTime, uId, amount, transactionType, transactionSubType, availableAmount,
				transactionId, remarks, createTimeStr);
	}


}