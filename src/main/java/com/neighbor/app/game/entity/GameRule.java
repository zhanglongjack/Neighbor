package com.neighbor.app.game.entity;

import com.neighbor.app.common.entity.PageEntity;

import java.util.Date;

public class GameRule extends PageEntity {
    private Long id;

    private Date createTime;

    private Date updateTime;

    private Long gameId;

    private Integer ruleType;//1:返佣规则,2:中奖规则,3:中雷规则

    private String matchingParam;//匹配的值

    private String ruleCode;

    private String ruleValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getMatchingParam() {
        return matchingParam;
    }

    public void setMatchingParam(String matchingParam) {
        this.matchingParam = matchingParam;
    }

    public Integer getRuleType() {
        return ruleType;
    }

    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getRuleValue() {
        return ruleValue;
    }

    public void setRuleValue(String ruleValue) {
        this.ruleValue = ruleValue;
    }

    @Override
    public String toString() {
        return "GameRule{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", gameId=" + gameId +
                ", ruleType=" + ruleType +
                ", matchingParam='" + matchingParam + '\'' +
                ", ruleCode='" + ruleCode + '\'' +
                ", ruleValue='" + ruleValue + '\'' +
                '}';
    }
}