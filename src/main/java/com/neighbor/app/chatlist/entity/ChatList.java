package com.neighbor.app.chatlist.entity;

import com.neighbor.common.util.PageTools;
import org.springframework.util.StringUtils;

import java.util.Date;

public class ChatList {
    private Long id;

    private Date createTime;

    private Date updateTime;

    private String creDate;

    private String creTime;

    private Long userId;

    private Long friendId;

    private Date lastChatDateTime;

    private String lastChatDate;

    private String lastChatTime;

    private Long lastChatMessageId;

    private String lastChatMessageType;

    private String lastChatMessageContent;

    private Long unreadMessageNum;

    private String toppingFlag;

    private String showMsgSwitch;

    private String chatHistorySet;

    private String friendNickName;
    private String friendHeadUrl;

    private String friendDesc;
    private String friendName;

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendDesc() {
        return friendDesc;
    }

    public void setFriendDesc(String friendDesc) {
        this.friendDesc = friendDesc;
    }

    private PageTools pageTools;

    public PageTools getPageTools() {
        return pageTools;
    }

    public void setPageTools(PageTools pageTools) {
        this.pageTools = pageTools;
    }

    public String getFriendNickName() {
        if(!StringUtils.isEmpty(friendDesc)){
            return friendDesc;
        }else if(!StringUtils.isEmpty(friendNickName)){
            return friendNickName;
        }else if(!StringUtils.isEmpty(friendName)){
            return friendName;
        }
        return friendId+"";
    }

    public void setFriendNickName(String friendNickName) {
        this.friendNickName = friendNickName;
    }

    public String getFriendHeadUrl() {
        return friendHeadUrl;
    }

    public void setFriendHeadUrl(String friendHeadUrl) {
        this.friendHeadUrl = friendHeadUrl;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
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

    public Long getUnreadMessageNum() {
        return unreadMessageNum;
    }

    public void setUnreadMessageNum(Long unreadMessageNum) {
        this.unreadMessageNum = unreadMessageNum;
    }

    public String getToppingFlag() {
        return toppingFlag;
    }

    public void setToppingFlag(String toppingFlag) {
        this.toppingFlag = toppingFlag;
    }

    public String getShowMsgSwitch() {
        return showMsgSwitch;
    }

    public void setShowMsgSwitch(String showMsgSwitch) {
        this.showMsgSwitch = showMsgSwitch;
    }

    public String getChatHistorySet() {
        return chatHistorySet;
    }

    public void setChatHistorySet(String chatHistorySet) {
        this.chatHistorySet = chatHistorySet;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ChatList{");
        sb.append("id=").append(id);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", creDate='").append(creDate).append('\'');
        sb.append(", creTime='").append(creTime).append('\'');
        sb.append(", userId=").append(userId);
        sb.append(", friendId=").append(friendId);
        sb.append(", lastChatDateTime=").append(lastChatDateTime);
        sb.append(", lastChatDate='").append(lastChatDate).append('\'');
        sb.append(", lastChatTime='").append(lastChatTime).append('\'');
        sb.append(", lastChatMessageId=").append(lastChatMessageId);
        sb.append(", lastChatMessageType='").append(lastChatMessageType).append('\'');
        sb.append(", lastChatMessageContent='").append(lastChatMessageContent).append('\'');
        sb.append(", unreadMessageNum=").append(unreadMessageNum);
        sb.append(", toppingFlag='").append(toppingFlag).append('\'');
        sb.append(", showMsgSwitch='").append(showMsgSwitch).append('\'');
        sb.append(", chatHistorySet='").append(chatHistorySet).append('\'');
        sb.append(", friendNickName='").append(friendNickName).append('\'');
        sb.append(", friendHeadUrl='").append(friendHeadUrl).append('\'');
        sb.append(", friendDesc='").append(friendDesc).append('\'');
        sb.append(", friendName='").append(friendName).append('\'');
        sb.append(", pageTools=").append(pageTools);
        sb.append('}');
        return sb.toString();
    }
}