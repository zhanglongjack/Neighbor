package com.neighbor.app.transfer.entity;

import com.neighbor.app.balance.po.TransactionItemDesc;
import com.neighbor.app.balance.po.TransactionSubTypeDesc;
import com.neighbor.app.balance.po.TransactionTypeDesc;
import com.neighbor.app.common.entity.PageEntity;
import com.neighbor.app.transfer.po.TransferStatusDesc;
import com.neighbor.app.transfer.po.TransferWayDesc;
import com.neighbor.common.util.DateFormateType;
import com.neighbor.common.util.DateUtils;
import com.neighbor.common.util.StringUtil;

import java.math.BigDecimal;
import java.util.Date;

public class Transfer extends PageEntity {
    private Long id;

    private String orderNo;

    private Date createTime;

    private String createTimeStr;

    private Date updateTime;

    private Long uId;

    private String transferWay;
    private String transferWayStr;

    private Long transferUserId;

    private BigDecimal amount;

    private String states;
    private String statesStr;

    private String remarks;

    private String beginTime;
    private String endTime;

    private BigDecimal availableAmount;

    private String transactionTypeDesc;

    public String getTransactionTypeDesc() {
        if(TransferWayDesc.in.toString().equals(transferWay)){
            return TransactionTypeDesc.receipt.getDes();
        }else{
            return TransactionTypeDesc.payment.getDes();
        }
    }

    public void setTransactionTypeDesc(String transactionTypeDesc) {
        this.transactionTypeDesc = transactionTypeDesc;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public String getTransferWay() {
        return transferWay;
    }

    public void setTransferWay(String transferWay) {
        this.transferWay = transferWay;
    }

    public Long getTransferUserId() {
        return transferUserId;
    }

    public void setTransferUserId(Long transferUserId) {
        this.transferUserId = transferUserId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public String getRemarks() {
        if(TransferWayDesc.in.toString().equals(transferWay)){
            return TransactionItemDesc.transfer.getDes()+StringUtil.split_
                    +TransactionSubTypeDesc.transferIn.getDes()+transferUserId;
        }else{
            return TransactionItemDesc.transfer.getDes()+StringUtil.split_
                    +TransactionSubTypeDesc.transferOut.getDes()+transferUserId;
        }
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
    public String getTransferWayStr() {
        if(transferWay!=null){
            return TransferWayDesc.getDesByValue(transferWay);
        }
        return transferWayStr;
    }

    public void setTransferWayStr(String transferWayStr) {
        this.transferWayStr = transferWayStr;
    }

    public String getStatesStr() {
        if(states!=null){
            return TransferStatusDesc.getDesByValue(states);
        }
        return statesStr;
    }

    public void setStatesStr(String statesStr) {
        this.statesStr = statesStr;
    }

    @Override
	public String toString() {
		return String.format(
				"Transfer [id=%s, orderNo=%s, createTime=%s, updateTime=%s, uId=%s, transferWay=%s, transferUserId=%s, amount=%s, states=%s, remarks=%s]",
				id, orderNo, createTime, updateTime, uId, transferWay, transferUserId, amount, states, remarks);
	}

 
    
    
    
}