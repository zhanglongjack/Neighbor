package com.neighbor.app.group.entity;

import com.neighbor.common.util.PageTools;

import java.util.Date;

public class GroupMember {
    private Long id;

    private Date createTime;

    private Date updateTime;

    private Long groupId;

    private Long userId;

    private String memberType;

    private String showMsgSwitch;

    private String showNickNameSwitch;

    private Long unreadMessageNum;

    private String states;

    private String automaticGrabSwitch;

    private String toppingFlag;

    private Date lastChatDateTime;

    private String lastChatDate;

    private String lastChatTime;

    private Long lastChatMessageId;

    private String lastChatMessageType;

    private String lastChatMessageContent;

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

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

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

    public Long getUnreadMessageNum() {
        return unreadMessageNum;
    }

    public void setUnreadMessageNum(Long unreadMessageNum) {
        this.unreadMessageNum = unreadMessageNum;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public String getAutomaticGrabSwitch() {
        return automaticGrabSwitch;
    }

    public void setAutomaticGrabSwitch(String automaticGrabSwitch) {
        this.automaticGrabSwitch = automaticGrabSwitch;
    }

    public String getToppingFlag() {
        return toppingFlag;
    }

    public void setToppingFlag(String toppingFlag) {
        this.toppingFlag = toppingFlag;
    }

    public Date getLastChatDateTime() {
        return lastChatDateTime;
    }

    public void setLastChatDateTime(Date lastChatDateTime) {
        this.lastChatDateTime = lastChatDateTime;
    }

    public String getLastChatDate() {
        return lastChatDate;
    }

    public void setLastChatDate(String lastChatDate) {
        this.lastChatDate = lastChatDate;
    }

    public String getLastChatTime() {
        return lastChatTime;
    }

    public void setLastChatTime(String lastChatTime) {
        this.lastChatTime = lastChatTime;
    }

    public Long getLastChatMessageId() {
        return lastChatMessageId;
    }

    public void setLastChatMessageId(Long lastChatMessageId) {
        this.lastChatMessageId = lastChatMessageId;
    }

    public String getLastChatMessageType() {
        return lastChatMessageType;
    }

    public void setLastChatMessageType(String lastChatMessageType) {
        this.lastChatMessageType = lastChatMessageType;
    }

    public String getLastChatMessageContent() {
        return lastChatMessageContent;
    }

    public void setLastChatMessageContent(String lastChatMessageContent) {
        this.lastChatMessageContent = lastChatMessageContent;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GroupMember{");
        sb.append("id=").append(id);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", groupId=").append(groupId);
        sb.append(", userId=").append(userId);
        sb.append(", memberType='").append(memberType).append('\'');
        sb.append(", showMsgSwitch='").append(showMsgSwitch).append('\'');
        sb.append(", showNickNameSwitch='").append(showNickNameSwitch).append('\'');
        sb.append(", unreadMessageNum=").append(unreadMessageNum);
        sb.append(", states='").append(states).append('\'');
        sb.append(", automaticGrabSwitch='").append(automaticGrabSwitch).append('\'');
        sb.append(", toppingFlag='").append(toppingFlag).append('\'');
        sb.append(", lastChatDateTime=").append(lastChatDateTime);
        sb.append(", lastChatDate='").append(lastChatDate).append('\'');
        sb.append(", lastChatTime='").append(lastChatTime).append('\'');
        sb.append(", lastChatMessageId=").append(lastChatMessageId);
        sb.append(", lastChatMessageType='").append(lastChatMessageType).append('\'');
        sb.append(", lastChatMessageContent='").append(lastChatMessageContent).append('\'');
        sb.append(", pageTools=").append(pageTools);
        sb.append('}');
        return sb.toString();
    }
}