package com.neighbor.app.group.entity;

import com.neighbor.common.util.PageTools;
import org.springframework.util.StringUtils;

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

    private PageTools pageTools;

    private Long friendUserId;

    private String friendNickName;
    private String friendHeadUrl;

    private String friendDesc;

    public Long getFriendUserId() {
        return friendUserId;
    }

    public void setFriendUserId(Long friendUserId) {
        this.friendUserId = friendUserId;
    }

    public String getFriendNickName() {
        if(!StringUtils.isEmpty(friendDesc)){
            return friendDesc;
        }else if(!StringUtils.isEmpty(friendNickName)){
            return friendNickName;
        }
        return userId+"";
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

    public String getFriendDesc() {
        return friendDesc;
    }

    public void setFriendDesc(String friendDesc) {
        this.friendDesc = friendDesc;
    }

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
        sb.append(", pageTools=").append(pageTools);
        sb.append('}');
        return sb.toString();
    }
}