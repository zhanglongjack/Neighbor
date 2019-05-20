package com.neighbor.app.pay.entity;

public class PayRespData {
    private String result_code;
    private String result_msg;
    private String method;
    private String out_trade_no;
    private String u_out_trade_no;
    private String code_url;
    private Long total;
    private Long create_time;

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getResult_msg() {
        return result_msg;
    }

    public void setResult_msg(String result_msg) {
        this.result_msg = result_msg;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
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

    public String getCode_url() {
        return code_url;
    }

    public void setCode_url(String code_url) {
        this.code_url = code_url;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }
}
