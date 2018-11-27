package com.neighbor.app.users.entity;

import java.util.Date;

public class UserConfig {
    private Long userId;

    private Date createTime;

    private Date updateTime;

    private String customerWindowShow;

    private String noPasswordPay;

    private String receiveNewMsg;

    private String showMsgDetail;

    private String haveVoice;

    private String haveShock;

    public UserConfig() {
        setCustomerWindowShow(CustomerWindowShowDesc.notShow.getValue());
        setNoPasswordPay(NoPasswordPayDesc.needPwd.getValue());
        setReceiveNewMsg(ReceiveNewMsgDesc.receive.getValue());
        setShowMsgDetail(ShowMsgDetailDesc.notShow.getValue());
        setHaveVoice(HaveVoiceDesc.noVoice.getValue());
        setHaveShock(HaveShockDesc.noShock.getValue());
    }

    public static enum CustomerWindowShowDesc {
        notShow("0"), show("1");
        public String value;

        private CustomerWindowShowDesc(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static enum NoPasswordPayDesc {
        needPwd("0"), noNeedPwd("1");
        public String value;

        private NoPasswordPayDesc(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static enum ReceiveNewMsgDesc {
        notReceive("0"), receive("1");
        public String value;

        private ReceiveNewMsgDesc(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static enum ShowMsgDetailDesc {
        notShow("0"), show("1");
        public String value;

        private ShowMsgDetailDesc(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static enum HaveVoiceDesc {
        noVoice("0"), haveVoice("1");
        public String value;

        private HaveVoiceDesc(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static enum HaveShockDesc {
        noShock("0"), haveShock("1");
        public String value;

        private HaveShockDesc(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getCustomerWindowShow() {
        return customerWindowShow;
    }

    public void setCustomerWindowShow(String customerWindowShow) {
        this.customerWindowShow = customerWindowShow;
    }

    public String getNoPasswordPay() {
        return noPasswordPay;
    }

    public void setNoPasswordPay(String noPasswordPay) {
        this.noPasswordPay = noPasswordPay;
    }

    public String getReceiveNewMsg() {
        return receiveNewMsg;
    }

    public void setReceiveNewMsg(String receiveNewMsg) {
        this.receiveNewMsg = receiveNewMsg;
    }

    public String getShowMsgDetail() {
        return showMsgDetail;
    }

    public void setShowMsgDetail(String showMsgDetail) {
        this.showMsgDetail = showMsgDetail;
    }

    public String getHaveVoice() {
        return haveVoice;
    }

    public void setHaveVoice(String haveVoice) {
        this.haveVoice = haveVoice;
    }

    public String getHaveShock() {
        return haveShock;
    }

    public void setHaveShock(String haveShock) {
        this.haveShock = haveShock;
    }
}