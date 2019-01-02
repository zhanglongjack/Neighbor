package com.neighbor.app.cms.entity;

import com.neighbor.app.common.entity.PageEntity;

import java.util.Date;

public class Cms extends PageEntity {
    private Long id;

    private Date createTime;

    private Date updateTime;

    private String contactDate;

    private String contactTime;

    private String cmsType;

    private String url;

    private String cmsDesc;

    private String jumpUrl;

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

    public String getCmsType() {
        return cmsType;
    }

    public void setCmsType(String cmsType) {
        this.cmsType = cmsType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCmsDesc() {
        return cmsDesc;
    }

    public void setCmsDesc(String cmsDesc) {
        this.cmsDesc = cmsDesc;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }
}