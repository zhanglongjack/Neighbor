package com.neighbor.app.packet.entity;

import java.math.BigDecimal;

public class LuckyMessage {
    private Long groupId;
    private Long userId;
    private BigDecimal lotteryAmount;
    private String nickName;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getLotteryAmount() {
        return lotteryAmount;
    }

    public void setLotteryAmount(BigDecimal lotteryAmount) {
        this.lotteryAmount = lotteryAmount;
    }
}
