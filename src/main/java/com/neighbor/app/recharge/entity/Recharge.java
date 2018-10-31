package com.neighbor.app.recharge.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.neighbor.app.common.entity.PageEntity;

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
        return remarks;
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