package com.neighbor.app.cms.entity;

import com.neighbor.app.common.entity.PageEntity;

import java.util.Date;

//内容管理
public class Cms extends PageEntity {

    private Long id;

    private Date createTime;

    private Date updateTime;

    private String contactDate;

    private String contactTime;

    //类型,1：图片；2：文字；
    private String cmsType;

    //位置,1：index页面；2：商城页面；
    private String position;


    //如果类型是图片的时候，这个值就是图片的地址
    private String url;

    private String cmsDesc;

    //跳转页面
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}