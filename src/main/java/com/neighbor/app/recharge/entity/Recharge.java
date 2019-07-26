package com.neighbor.app.recharge.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.neighbor.app.balance.po.TransactionItemDesc;
import com.neighbor.app.balance.po.TransactionTypeDesc;
import com.neighbor.app.common.entity.PageEntity;
import com.neighbor.app.pay.constants.MethodDesc;
import com.neighbor.app.recharge.constants.ChannelTypeDesc;
import com.neighbor.app.recharge.constants.RechargeStatusDesc;
import com.neighbor.common.util.DateFormateType;
import com.neighbor.common.util.DateUtils;
import org.springframework.util.StringUtils;

public class Recharge  extends PageEntity{
    private Long id;
    private Long recordId;

    private String orderNo;

    private Date createTime;

    private Date updateTime;

    private Long uId;

    private String channelType;

    private BigDecimal amount;

    private String states;

    private String remarks;

    private BigDecimal availableAmount;
    
    private String createTimeStr ;
    private String statesDesc ;
    private String channelTypeDesc ;

    private String phone;
    private String verfiyCode;

    private String method; //接口列表
    private String methodDesc;
    private String body; //商品描述
    private String payState;//支付状态 支付状态 0未支付/待支付 1支付成功 2退款成功
    private String payStateDesc;
    private String outTradeNo;//支付渠道订单号
    private String transactionId;//支付官方交易流水
    private String codeUrl;//支付二维码 支付一次无效
    private String h5Url;//支付宝H5二维码 支付一次无效
    private String channelNo;

    private String payAction = "page";//支付动作(默认：page 页面跳转，app,app支付)

    public String getPayAction() {
        return payAction;
    }

    public void setPayAction(String payAction) {
        this.payAction = payAction;
    }

    public String getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo;
    }

    public String getMethodDesc() {
        if(!StringUtils.isEmpty(method)){
            return MethodDesc.getDesByValue(method);
        }
        return "线下转账";
    }

    public void setMethodDesc(String methodDesc) {
        this.methodDesc = methodDesc;
    }

    public String getPayStateDesc() {
        if("0".equals(payState)){
            return "未支付";
        }else if("1".equals(payState)){
            return "支付成功";
        }else if("2".equals(payState)){
            return "退款成功";
        }
        return payStateDesc;
    }

    public void setPayStateDesc(String payStateDesc) {
        this.payStateDesc = payStateDesc;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerfiyCode() {
        return verfiyCode;
    }

    public void setVerfiyCode(String verfiyCode) {
        this.verfiyCode = verfiyCode;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public void setStatesDesc(String statesDesc) {
        this.statesDesc = statesDesc;
    }

    public void setChannelTypeDesc(String channelTypeDesc) {
        this.channelTypeDesc = channelTypeDesc;
    }

    private String transactionTypeDesc;

    private String isCustomer;

    public String getIsCustomer() {
        return isCustomer;
    }

    public void setIsCustomer(String isCustomer) {
        this.isCustomer = isCustomer;
    }

    private String screenshot;//线下转账截图地址

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }


    public String getTransactionTypeDesc() {
        return TransactionTypeDesc.receipt.getDes();
    }

    public void setTransactionTypeDesc(String transactionTypeDesc) {
        this.transactionTypeDesc = transactionTypeDesc;
    }

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
    
    public String getStatesDesc() {
        if(states!=null){
            return RechargeStatusDesc.getDesByValue(states);
        }
        return statesDesc;
    }
    
    public String getChannelTypeDesc() {
        if(channelType!=null){
            ChannelTypeDesc.getDesByValue(channelType);
        }
        return channelTypeDesc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }

    public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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

    public Long getuId() {
        return uId;
    }

    public void setuId(Long uId) {
        this.uId = uId;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public String getRemarks() {
        if(!StringUtils.isEmpty(remarks)){
            return remarks;
        }
        if(channelType!=null){
            return ChannelTypeDesc.getDesByValue(channelType)+TransactionItemDesc.recharge.getDes();
        }
        return TransactionItemDesc.recharge.getDes();
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    @Override
    public String toString() {
        return "Recharge{" +
                "id=" + id +
                ", orderNo='" + orderNo + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", uId=" + uId +
                ", channelType='" + channelType + '\'' +
                ", amount=" + amount +
                ", states='" + states + '\'' +
                ", remarks='" + remarks + '\'' +
                ", availableAmount=" + availableAmount +
                ", createTimeStr='" + createTimeStr + '\'' +
                ", statesDesc='" + statesDesc + '\'' +
                ", channelTypeDesc='" + channelTypeDesc + '\'' +
                ", transactionTypeDesc='" + transactionTypeDesc + '\'' +
                ", isCustomer='" + isCustomer + '\'' +
                ", screenshot='" + screenshot + '\'' +
                '}';
    }
}