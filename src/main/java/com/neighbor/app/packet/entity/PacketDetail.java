package com.neighbor.app.packet.entity;

import java.math.BigDecimal;

import com.neighbor.app.common.entity.PageEntity;
import com.neighbor.common.util.DateUtils;

public class PacketDetail extends PageEntity{

	private Long id;

    private Long dPacketId;

    private Long gotUserId;
    private String headUrl;
    private String nickName;

    private BigDecimal gotAmount;

    private Boolean isGotBomb;

    private Boolean isFree;

    private Boolean isMaximum;

    private String createDate = DateUtils.getStringDateShort();

    private String createTime = DateUtils.getTimeShort();
    
    private Packet packet;
    
    //query
    private String createYear;
    
    
    public String getCreateYear() {
		return createYear;
	}

	public void setCreateYear(String createYear) {
		this.createYear = createYear;
	}

	public Packet getPacket() {
		return packet;
	}

	public void setPacket(Packet packet) {
		this.packet = packet;
	}

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

    public Boolean isGotBomb() {
		return isGotBomb;
	}

	public void setGotBomb(Boolean isGotBomb) {
		this.isGotBomb = isGotBomb;
	}

	public Boolean isFree() {
		return isFree;
	}

	public void setFree(Boolean isFree) {
		this.isFree = isFree;
	}

	public Boolean isMaximum() {
		return isMaximum;
	}

	public void setMaximum(Boolean isMaximum) {
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

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@Override
	public String toString() {
		return String.format(
				"PacketDetail [id=%s, dPacketId=%s, gotUserId=%s, headUrl=%s, nickName=%s, gotAmount=%s, isGotBomb=%s, isFree=%s, isMaximum=%s, createDate=%s, createTime=%s, packet=%s, createYear=%s]",
				id, dPacketId, gotUserId, headUrl, nickName, gotAmount, isGotBomb, isFree, isMaximum, createDate,
				createTime, packet, createYear);
	}


}