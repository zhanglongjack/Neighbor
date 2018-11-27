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
        setCustomerWindowShow(CustomerWindowShowDesc.不显示客服窗口.getValue());
        setNoPasswordPay(UserConfig.NoPasswordPayDesc.需要支付密码.getValue());
        setReceiveNewMsg(UserConfig.ReceiveNewMsgDesc.接收新消息.getValue());
        setShowMsgDetail(UserConfig.ShowMsgDetailDesc.不显示消息详情.getValue());
        setHaveVoice(UserConfig.HaveVoiceDesc.没有声音.getValue());
        setHaveShock(UserConfig.HaveShockDesc.没有震动.getValue());
    }

    public static enum CustomerWindowShowDesc {
        不显示客服窗口("0"), 显示客服窗口("1");
        public String value;

        private CustomerWindowShowDesc(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static enum NoPasswordPayDesc {
        需要支付密码("0"), 不需要支付密码("1");
        public String value;

        private NoPasswordPayDesc(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static enum ReceiveNewMsgDesc {
        不接收新消息("0"), 接收新消息("1");
        public String value;

        private ReceiveNewMsgDesc(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static enum ShowMsgDetailDesc {
        不显示消息详情("0"), 显示消息详情("1");
        public String value;

        private ShowMsgDetailDesc(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static enum HaveVoiceDesc {
        没有声音("0"), 有声音("1");
        public String value;

        private HaveVoiceDesc(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static enum HaveShockDesc {
        没有震动("0"), 有震动("1");
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