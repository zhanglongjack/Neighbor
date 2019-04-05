package com.neighbor.app.group.entity;

import com.neighbor.common.util.PageTools;

import java.util.Date;

public class Group {
    private Long id;
    private Long groupId;


    private Date createTime;

    private Date updateTime;

    private String creDate;

    private String creTime;

    private String groupName;

    private Long userNum;

    private Long onlineNum;

    private String groupNotice;

    private String groupHeadImgUrl;

    private String states;

    private Long gameId;
    private String redPackAmountLimit;

    private Long userId;
    private String gameType;//游戏类型（1:红包游戏,2:猜猜乐,3:休闲游戏,4:福利群）

    private Long groupOwnerUserId;//群主ID

    private String showMsgSwitch;

    private String showNickNameSwitch;

    public String getShowMsgSwitch() {
        return showMsgSwitch;
    }

    public void setShowMsgSwitch(String showMsgSwitch) {
        this.showMsgSwitch = showMsgSwitch;
    }

    public String getShowNickNameSwitch() {
        return showNickNameSwitch;
    }

    public void setShowNickNameSwitch(String showNickNameSwitch) {
        this.showNickNameSwitch = showNickNameSwitch;
    }

    public Long getGroupOwnerUserId() {
        return groupOwnerUserId;
    }

    public void setGroupOwnerUserId(Long groupOwnerUserId) {
        this.groupOwnerUserId = groupOwnerUserId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getRedPackAmountLimit() {
        return redPackAmountLimit;
    }

    public void setRedPackAmountLimit(String redPackAmountLimit) {
        this.redPackAmountLimit = redPackAmountLimit;
    }

    private PageTools pageTools;

    public PageTools getPageTools() {
        return pageTools;
    }

    public void setPageTools(PageTools pageTools) {
        this.pageTools = pageTools;
    }



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

    public String getCreDate() {
        return creDate;
    }

    public void setCreDate(String creDate) {
        this.creDate = creDate;
    }

    public String getCreTime() {
        return creTime;
    }

    public void setCreTime(String creTime) {
        this.creTime = creTime;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getUserNum() {
        return userNum;
    }

    public void setUserNum(Long userNum) {
        this.userNum = userNum;
    }

    public Long getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(Long onlineNum) {
        this.onlineNum = onlineNum;
    }

    public String getGroupNotice() {
        return groupNotice;
    }

    public void setGroupNotice(String groupNotice) {
        this.groupNotice = groupNotice;
    }

    public String getGroupHeadImgUrl() {
        return groupHeadImgUrl;
    }

    public void setGroupHeadImgUrl(String groupHeadImgUrl) {
        this.groupHeadImgUrl = groupHeadImgUrl;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        if(groupId!=null){
           this.id=groupId;
        }
        this.groupId = groupId;
    }

    private Long enterUserId;

    public Long getEnterUserId() {
        return enterUserId;
    }

    public void setEnterUserId(Long enterUserId) {
        this.enterUserId = enterUserId;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", creDate='" + creDate + '\'' +
                ", creTime='" + creTime + '\'' +
                ", groupName='" + groupName + '\'' +
                ", userNum=" + userNum +
                ", onlineNum=" + onlineNum +
                ", groupNotice='" + groupNotice + '\'' +
                ", groupHeadImgUrl='" + groupHeadImgUrl + '\'' +
                ", states='" + states + '\'' +
                ", gameId=" + gameId +
                ", redPackAmountLimit='" + redPackAmountLimit + '\'' +
                ", pageTools=" + pageTools +
                '}';
    }
}