package com.neighbor.app.recharge.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.neighbor.app.balance.po.TransactionItemDesc;
import com.neighbor.app.balance.po.TransactionTypeDesc;
import com.neighbor.app.common.entity.PageEntity;
import com.neighbor.app.recharge.po.ChannelTypeDesc;
import com.neighbor.app.recharge.po.RechargeStatusDesc;
import com.neighbor.common.util.DateFormateType;
import com.neighbor.common.util.DateUtils;
import com.neighbor.common.util.StringUtil;

public class Recharge  extends PageEntity{
    private Long id;

    private String orderNo;

    private Date createTime;

    private Date updateTime;

    private Long uId;

    private String channelType;

    private BigDecimal amount;

    private String states;

    private String remarks;

    private BigDecimal availableAmount;
    
    private String createTimeStr ;
    private String statesDesc ;
    private String channelTypeDesc ;

    private String transactionTypeDesc;


    public String getTransactionTypeDesc() {
        return TransactionTypeDesc.receipt.getDes();
    }

    public void setTransactionTypeDesc(String transactionTypeDesc) {
        this.transactionTypeDesc = transactionTypeDesc;
    }

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
    
    public String getStatesDesc() {
        if(states!=null){
            return RechargeStatusDesc.getDesByValue(states);
        }
        return statesDesc;
    }
    
    public String getChannelTypeDesc() {
        if(channelType!=null){
            ChannelTypeDesc.getDesByValue(channelType);
        }
        return channelTypeDesc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
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

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
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
        if(channelType!=null){
            return ChannelTypeDesc.getDesByValue(channelType)+TransactionItemDesc.recharge.getDes();
        }
        return TransactionItemDesc.transfer.getDes();
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

	@Override
	public String toString() {
		return String.format(
				"Recharge [id=%s, orderNo=%s, createTime=%s, updateTime=%s, uId=%s, channelType=%s, amount=%s, states=%s, remarks=%s]",
				id, orderNo, createTime, updateTime, uId, channelType, amount, states, remarks);
	}


    
    
   
    
}