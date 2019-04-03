package com.neighbor.app.packet.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.neighbor.app.common.entity.PageEntity;

public class Packet extends PageEntity{
    private Long id;

    private Long userId;
    
    private String headUrl;

    private Long receiveUserId;

    private Long groupId;

    private BigDecimal amount;

    private Integer packetNum;

    private Integer hitNum;

    private String sendDate;

    private String sendTime;
    
    private String status;
    
    private Integer collectedNum;

    private String remarke;
    private String nickName;
    
    private String randomAmount = "";
    private String randomAmountList[] = null;
    
    private List<PacketDetail> detailList = new ArrayList<PacketDetail>();

    //query
    private String createYear;
    private String groupIdIsNotNull;
    private String sendDateLess;
    private String sendTimeLess;
    
    public String getCreateYear() {
		return createYear;
	}

	public void setCreateYear(String createYear) {
		this.createYear = createYear;
	}

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

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
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

    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getCollectedNum() {
		return collectedNum;
	}

	public void setCollectedNum(Integer collectedNum) {
		this.collectedNum = collectedNum;
	}

	public String getRemarke() {
        return remarke;
    }

    public void setRemarke(String remarke) {
        this.remarke = remarke;
    }

	public String getRandomAmount() {
		return randomAmount;
	}

	public void setRandomAmount(String randomAmount) {
		this.randomAmount = randomAmount;
		randomAmountList = randomAmount.split(",");
	}

	public String[] getRandomAmountList() {
		return randomAmountList;
	}

	public void setRandomAmountList(String[] randomAmountList) {
		this.randomAmountList = randomAmountList;
	}

	public List<PacketDetail> getDetailList() {
		return detailList;
	}
	
	public void setDetailList(List<PacketDetail> detailList) {
		this.detailList = detailList;
	}

	public void addDetail(PacketDetail detail){
		detailList.add(detail);
	}

	
	public String getGroupIdIsNotNull() {
		return groupIdIsNotNull;
	}

	public void setGroupIdIsNotNull(String groupIdIsNotNull) {
		this.groupIdIsNotNull = groupIdIsNotNull;
	}

	public String getSendDateLess() {
		return sendDateLess;
	}

	public void setSendDateLess(String sendDateLess) {
		this.sendDateLess = sendDateLess;
	}

	public String getSendTimeLess() {
		return sendTimeLess;
	}

	public void setSendTimeLess(String sendTimeLess) {
		this.sendTimeLess = sendTimeLess;
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
				"Packet [id=%s, userId=%s, headUrl=%s, receiveUserId=%s, groupId=%s, amount=%s, packetNum=%s, hitNum=%s, sendDate=%s, sendTime=%s, status=%s, collectedNum=%s, remarke=%s, nickName=%s, randomAmount=%s, randomAmountList=%s, detailList=%s, createYear=%s, groupIdIsNotNull=%s, sendDateLess=%s, sendTimeLess=%s]",
				id, userId, headUrl, receiveUserId, groupId, amount, packetNum, hitNum, sendDate, sendTime, status,
				collectedNum, remarke, nickName, randomAmount, Arrays.toString(randomAmountList), detailList,
				createYear, groupIdIsNotNull, sendDateLess, sendTimeLess);
	}




    
}