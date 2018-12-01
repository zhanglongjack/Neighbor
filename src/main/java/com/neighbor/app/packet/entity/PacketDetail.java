package com.neighbor.app.packet.entity;

import java.math.BigDecimal;

import com.neighbor.common.util.DateUtils;

public class PacketDetail {
    private Long id;

    private Long dPacketId;

    private Long gotUserId;

    private BigDecimal gotAmount;

    private String isGotBomb;

    private String isFree;

    private String isMaximum;

    private String createDate = DateUtils.getStringDateShort();

    private String createTime = DateUtils.getTimeShort();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getdPacketId() {
        return dPacketId;
    }

    public void setdPacketId(Long dPacketId) {
        this.dPacketId = dPacketId;
    }

    public Long getGotUserId() {
        return gotUserId;
    }

    public void setGotUserId(Long gotUserId) {
        this.gotUserId = gotUserId;
    }

    public BigDecimal getGotAmount() {
        return gotAmount;
    }

    public void setGotAmount(BigDecimal gotAmount) {
        this.gotAmount = gotAmount;
    }

    public String getIsGotBomb() {
        return isGotBomb;
    }

    public void setIsGotBomb(String isGotBomb) {
        this.isGotBomb = isGotBomb;
    }

    public String getIsFree() {
        return isFree;
    }

    public void setIsFree(String isFree) {
        this.isFree = isFree;
    }

    public String getIsMaximum() {
        return isMaximum;
    }

    public void setIsMaximum(String isMaximum) {
        this.isMaximum = isMaximum;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}