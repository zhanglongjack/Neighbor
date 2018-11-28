package com.neighbor.app.packet.entity;

import java.math.BigDecimal;

public class Packet {
    private Long id;

    private Long userId;

    private Long receiveUserId;

    private Long groupId;

    private BigDecimal amount;

    private Integer packetNum;

    private Integer hitNum;

    private String sendDate;

    private String sendTime;

    private String remarke;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(Long receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getPacketNum() {
        return packetNum;
    }

    public void setPacketNum(Integer packetNum) {
        this.packetNum = packetNum;
    }

    public Integer getHitNum() {
        return hitNum;
    }

    public void setHitNum(Integer hitNum) {
        this.hitNum = hitNum;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getRemarke() {
        return remarke;
    }

    public void setRemarke(String remarke) {
        this.remarke = remarke;
    }

	@Override
	public String toString() {
		return String.format(
				"Packet [id=%s, userId=%s, receiveUserId=%s, groupId=%s, amount=%s, packetNum=%s, hitNum=%s, sendDate=%s, sendTime=%s, remarke=%s]",
				id, userId, receiveUserId, groupId, amount, packetNum, hitNum, sendDate, sendTime, remarke);
	}
    
    
}