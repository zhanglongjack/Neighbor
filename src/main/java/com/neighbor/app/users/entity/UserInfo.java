package com.neighbor.app.users.entity;

import java.util.Date;

public class UserInfo {
    private Long id;

    private String userPhoto;

    private String nickName;

    private String userAccount;

    private String userPassword;

    private String qrCode;

    private String sex;

    private String mobilePhone;

    private String realName;

    private String wechat;

    private String qq;

    private String robotSno;

    private Long upuserId;

    private Date createTime;

    private Date updateTime;

    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getRobotSno() {
        return robotSno;
    }

    public void setRobotSno(String robotSno) {
        this.robotSno = robotSno;
    }

    public Long getUpuserId() {
        return upuserId;
    }

    public void setUpuserId(Long upuserId) {
        this.upuserId = upuserId;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserInfo [id=");
		builder.append(id);
		builder.append(", userPhoto=");
		builder.append(userPhoto);
		builder.append(", nickName=");
		builder.append(nickName);
		builder.append(", userAccount=");
		builder.append(userAccount);
		builder.append(", userPassword=");
		builder.append(userPassword);
		builder.append(", qrCode=");
		builder.append(qrCode);
		builder.append(", sex=");
		builder.append(sex);
		builder.append(", mobilePhone=");
		builder.append(mobilePhone);
		builder.append(", realName=");
		builder.append(realName);
		builder.append(", wechat=");
		builder.append(wechat);
		builder.append(", qq=");
		builder.append(qq);
		builder.append(", robotSno=");
		builder.append(robotSno);
		builder.append(", upuserId=");
		builder.append(upuserId);
		builder.append(", createTime=");
		builder.append(createTime);
		builder.append(", updateTime=");
		builder.append(updateTime);
		builder.append(", remark=");
		builder.append(remark);
		builder.append("]");
		return builder.toString();
	}

    
    
}