package com.neighbor.app.recharge.po;

import com.neighbor.common.util.DateFormateType;
import com.neighbor.common.util.DateUtils;
import java.util.Date;
import java.math.BigDecimal;

public class RechargeRecord {
    private Long id;
    private String orderNo;
    private Date createTime;
    private BigDecimal amount;
    private BigDecimal availableAmount;
    private String channelType;
    private String states;
    private String remarks;
    private String channelTypeDesc;
    private String createTimeStr;
    private String statesDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String getStatesDesc() {
        if(states!=null){
            return RechargeStatusDesc.getDesByValue(states);
        }
        return statesDesc;
    }

    public void setStatesDesc(String statesDesc) {
        this.statesDesc = statesDesc;
    }

    public String getChannelTypeDesc() {
        if(channelType!=null){
            return ChannelTypeDesc.getDesByValue(channelType);
        }
        return channelTypeDesc;
    }



    public void setChannelTypeDesc(String channelTypeDesc) {
        this.channelTypeDesc = channelTypeDesc;
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


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
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

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }
}
