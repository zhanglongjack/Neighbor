package com.neighbor.app.game.entity;

import com.neighbor.app.common.entity.PageEntity;
import com.neighbor.app.game.constants.RuleSubTypeDesc;
import com.neighbor.app.game.constants.RuleTypeDesc;
import com.neighbor.common.util.DateFormateType;
import com.neighbor.common.util.DateUtils;
import com.neighbor.common.util.StringUtil;

import java.util.Date;

public class GameRule extends PageEntity {
    private Long id;

    private Date createTime;
    private String createTimeStr;

    private Date updateTime;

    private Long gameId;

    private Integer ruleType;//1:返佣规则,2:中奖规则,3:中雷规则
    private String ruleTypeStr;

    private String matchingParam;//匹配的值

    private String ruleCode;

    private String ruleValue;

    private String ruleSubType;// 1:单个值,2:顺子,3:同数
    private String ruleSubTypeStr;


    private String schemeCode;//确定值值列表

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


    public String getRuleSubType() {
        return ruleSubType;
    }

    public void setRuleSubType(String ruleSubType) {
        this.ruleSubType = ruleSubType;
    }

    public String getSchemeCode() {
        return schemeCode;
    }

    public void setSchemeCode(String schemeCode) {
        this.schemeCode = schemeCode;
    }

    public String getRuleTypeStr() {
        if(ruleType!=null){
            return RuleTypeDesc.getRuleTypeStr(ruleType);
        }
        return ruleTypeStr;
    }

    public void setRuleTypeStr(String ruleTypeStr) {
        this.ruleTypeStr = ruleTypeStr;
    }

    public String getRuleSubTypeStr() {
        if(StringUtil.isNotEmpty(ruleSubType)){
            return RuleSubTypeDesc.getRuleSubTypeStr(Integer.valueOf(ruleSubType));
        }
        return ruleSubTypeStr;
    }

    public void setRuleSubTypeStr(String ruleSubTypeStr) {
        this.ruleSubTypeStr = ruleSubTypeStr;
    }

    public String getCreateTimeStr() {
        if(createTime!=null){
            return DateUtils.formatDateStr(createTime, DateFormateType.LANG_FORMAT);
        }
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
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