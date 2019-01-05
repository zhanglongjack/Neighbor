package com.neighbor.app.group.entity;

import com.neighbor.app.common.entity.PageEntity;

import java.util.Date;

public class GroupApply extends PageEntity {
    private Long id;

    private Date createTime;

    private Date updateTime;

    private Long groupId;

    private Long enterUserId;

    private Long inviteUserId;

    private String states;

    private String showFlag;

    private Long recordId;

    /******  补充字段 *****/
    private String friendName;
    private String friendNickName;
    private String userPhoto;

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
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

    public Long getEnterUserId() {
        return enterUserId;
    }

    public void setEnterUserId(Long enterUserId) {
        this.enterUserId = enterUserId;
    }

    public Long getInviteUserId() {
        return inviteUserId;
    }

    public void setInviteUserId(Long inviteUserId) {
        this.inviteUserId = inviteUserId;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public String getShowFlag() {
        return showFlag;
    }

    public void setShowFlag(String showFlag) {
        this.showFlag = showFlag;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendNickName() {
        return friendNickName;
    }

    public void setFriendNickName(String friendNickName) {
        this.friendNickName = friendNickName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    @Override
    public String toString() {
        return "GroupApply{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", groupId=" + groupId +
                ", enterUserId=" + enterUserId +
                ", inviteUserId=" + inviteUserId +
                ", states='" + states + '\'' +
                ", showFlag='" + showFlag + '\'' +
                ", recordId=" + recordId +
                ", friendName='" + friendName + '\'' +
                ", friendNickName='" + friendNickName + '\'' +
                ", userPhoto='" + userPhoto + '\'' +
                '}';
    }
}