package com.neighbor.app.friend.entity;

import com.neighbor.app.common.entity.PageEntity;

import java.util.Date;

public class FriendApply extends PageEntity {
    private Long id;
    private Date createTime;

    private Date updateTime;

    private String contactDate;

    private String contactTime;

    private Long userId;

    private Long friendUserId;

    private String friendDesc;

    //状态，1：申请；2：审核通过；3：审核拒绝；
    private String states;

    private String addDirection;
    private String addType;

    /******  补充字段 *****/
    private String friendName;
    private String friendNickName;
    private String userPhoto;


    public static enum StatesDesc {
        申请中("1"), 审核通过("2"), 审核拒绝("3");
        public String value;

        private StatesDesc(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static enum AddDirectionDesc {
        主动添加("1"), 被动添加("2");
        public String value;

        private AddDirectionDesc(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static enum AddTypeDesc {
        链接添加("1"), APP添加("2");
        public String value;

        private AddTypeDesc(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
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

    public String getContactDate() {
        return contactDate;
    }

    public void setContactDate(String contactDate) {
        this.contactDate = contactDate;
    }

    public String getContactTime() {
        return contactTime;
    }

    public void setContactTime(String contactTime) {
        this.contactTime = contactTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFriendUserId() {
        return friendUserId;
    }

    public void setFriendUserId(Long friendUserId) {
        this.friendUserId = friendUserId;
    }

    public String getFriendDesc() {
        return friendDesc;
    }

    public void setFriendDesc(String friendDesc) {
        this.friendDesc = friendDesc;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public String getAddDirection() {
        return addDirection;
    }

    public void setAddDirection(String addDirection) {
        this.addDirection = addDirection;
    }

    public String getAddType() {
        return addType;
    }

    public void setAddType(String addType) {
        this.addType = addType;
    }
}