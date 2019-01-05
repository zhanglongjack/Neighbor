package com.neighbor.app.packet.entity;

import java.math.BigDecimal;

import com.neighbor.common.util.DateUtils;

public class PacketDetail {
    private Long id;

    private Long dPacketId;

    private Long gotUserId;

    private BigDecimal gotAmount;

    private boolean isGotBomb;

    private boolean isFree;

    private boolean isMaximum;

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

    public boolean isGotBomb() {
		return isGotBomb;
	}

	public void setGotBomb(boolean isGotBomb) {
		this.isGotBomb = isGotBomb;
	}

	public boolean isFree() {
		return isFree;
	}

	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}

	public boolean isMaximum() {
		return isMaximum;
	}

	public void setMaximum(boolean isMaximum) {
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