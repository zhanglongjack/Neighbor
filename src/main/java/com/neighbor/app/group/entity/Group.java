package com.neighbor.app.group.entity;

import com.neighbor.common.util.PageTools;

import java.util.Date;

public class Group {
    private Long id;

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


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Group{");
        sb.append("id=").append(id);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", creDate='").append(creDate).append('\'');
        sb.append(", creTime='").append(creTime).append('\'');
        sb.append(", groupName='").append(groupName).append('\'');
        sb.append(", userNum=").append(userNum);
        sb.append(", onlineNum=").append(onlineNum);
        sb.append(", groupNotice='").append(groupNotice).append('\'');
        sb.append(", groupHeadImgUrl='").append(groupHeadImgUrl).append('\'');
        sb.append(", states='").append(states).append('\'');
        sb.append(", pageTools=").append(pageTools);
        sb.append('}');
        return sb.toString();
    }
}