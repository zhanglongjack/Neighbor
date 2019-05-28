package com.neighbor.app.packet.entity;

import java.math.BigDecimal;

public class LuckyMessage {
    private Long groupId;
    private Long userId;
    private BigDecimal lotteryAmount;

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
