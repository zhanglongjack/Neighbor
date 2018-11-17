package com.neighbor.app.bankcard.entity;

import com.neighbor.app.common.entity.PageEntity;

import java.util.Date;

public class BankCard  extends PageEntity {
    private Long id;

    private Date createTime;

    private Date updateTime;

    private String bindDate;

    private String bindTime;

    private Long uId;

    private String bankCode;

    private String bankName;

    private String cardType;

    private String cardTypeName;

    private String bankCardNo;

    private String bankCardEndNo;

    private String branchInfo;

    private String realName;

    private String remarks;

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

    public String getBindDate() {
        return bindDate;
    }

    public void setBindDate(String bindDate) {
        this.bindDate = bindDate;
    }

    public String getBindTime() {
        return bindTime;
    }

    public void setBindTime(String bindTime) {
        this.bindTime = bindTime;
    }

    public Long getuId() {
        return uId;
    }

    public void setuId(Long uId) {
        this.uId = uId;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardTypeName() {
        return cardTypeName;
    }

    public void setCardTypeName(String cardTypeName) {
        this.cardTypeName = cardTypeName;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getBankCardEndNo() {
        return bankCardEndNo;
    }

    public void setBankCardEndNo(String bankCardEndNo) {
        this.bankCardEndNo = bankCardEndNo;
    }

    public String getBranchInfo() {
        return branchInfo;
    }

    public void setBranchInfo(String branchInfo) {
        this.branchInfo = branchInfo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BankCard{");
        sb.append("id=").append(id);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", bindDate='").append(bindDate).append('\'');
        sb.append(", bindTime='").append(bindTime).append('\'');
        sb.append(", uId=").append(uId);
        sb.append(", bankCode='").append(bankCode).append('\'');
        sb.append(", bankName='").append(bankName).append('\'');
        sb.append(", cardType='").append(cardType).append('\'');
        sb.append(", cardTypeName='").append(cardTypeName).append('\'');
        sb.append(", bankCardNo='").append(bankCardNo).append('\'');
        sb.append(", bankCardEndNo='").append(bankCardEndNo).append('\'');
        sb.append(", branchInfo='").append(branchInfo).append('\'');
        sb.append(", realName='").append(realName).append('\'');
        sb.append(", remarks='").append(remarks).append('\'');
        sb.append('}');
        return sb.toString();
    }
}