package com.neighbor.app.users.entity;

import java.util.Date;

import com.neighbor.app.common.entity.PageEntity;
import com.neighbor.common.util.DateFormateType;
import com.neighbor.common.util.DateUtils;

public class UserInfo extends PageEntity {
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

    private String regional;
    
    private Long upUserId;

    private Date createTime;

    private Date updateTime;

    private String remark;
    private String createTimeStr ;
    private Long downNumber ;
    
    public String getCreateTimeStr() {
        if(createTime!=null){
            try {
                return DateUtils.formatDateStr(createTime,DateFormateType.LANG_FORMAT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return createTimeStr;
    }
    
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

    public Long getUpUserId() {
		return upUserId;
	}

	public void setUpUserId(Long upUserId) {
		this.upUserId = upUserId;
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

	public String getRegional() {
		return regional;
	}

	public void setRegional(String regional) {
		this.regional = regional;
	}

	public Long getDownNumber() {
		return downNumber;
	}

	public void setDownNumber(Long downNumber) {
		this.downNumber = downNumber;
	}

	@Override
	public String toString() {
		return String.format(
				"UserInfo [id=%s, userPhoto=%s, nickName=%s, userAccount=%s, userPassword=%s, qrCode=%s, sex=%s, mobilePhone=%s, realName=%s, wechat=%s, qq=%s, robotSno=%s, regional=%s, upUserId=%s, createTime=%s, updateTime=%s, remark=%s, createTimeStr=%s, downNumber=%s]",
				id, userPhoto, nickName, userAccount, userPassword, qrCode, sex, mobilePhone, realName, wechat, qq,
				robotSno, regional, upUserId, createTime, updateTime, remark, createTimeStr, downNumber);
	}

}