package com.neighbor.app.pay.entity;
/*
appid	商户号终端APPID
method	接口支付类型参考各接口method
status	支付状态 0未支付/待支付 1支付成功 2退款成功
out_trade_no	平台订单号
u_out_trade_no	商户订单号
transaction_id	官方订单号成功状态订单不为空
total_fee	交易金额单位:分
create_time	订单创建时间
nonce_str	随机字符串
sign	数据签名参考数据签名规则
 */
public class NotifyResp {
    private String appid;
    private String method;
    private String status;
    private String out_trade_no;
    private String u_out_trade_no;
    private String transaction_id;
    private Long total_fee;
    private String create_time;
    private String nonce_str;
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getU_out_trade_no() {
        return u_out_trade_no;
    }

    public void setU_out_trade_no(String u_out_trade_no) {
        this.u_out_trade_no = u_out_trade_no;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public Long getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(Long total_fee) {
        this.total_fee = total_fee;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
